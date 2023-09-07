package com.example.myittaroostockinventorymanger.ui.item_name

import android.os.Build
import android.view.ActionMode
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.data.entities.Stock
import com.example.myittaroostockinventorymanger.data.repository.Repository
import com.example.myittaroostockinventorymanger.event.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Locale
import java.util.stream.Collectors

class ItemNameViewModel : ViewModel() {
    private var mutStockList: LiveData<List<Stock>>
    private val message: MutableLiveData<Event<String?>>
    private val isLoading: MutableLiveData<Boolean>
    private val mutFilterNames: MutableLiveData<List<Stock>>
    private val contextualActionBarTitle: MutableLiveData<String>
    private val repository: Repository
    private val compositeDisposable = CompositeDisposable()
    private val isShowRenameButton: MutableLiveData<Boolean>
    private val isValidDelete: MutableLiveData<Boolean>

    init {
        mutStockList = MutableLiveData()
        repository = Repository()
        message = MutableLiveData()
        isLoading = MutableLiveData(false)
        mutFilterNames = MutableLiveData()
        contextualActionBarTitle = MutableLiveData()
        isShowRenameButton = MutableLiveData()
        isValidDelete = MutableLiveData()
    }

    fun getAllItems(): LiveData<List<Stock>> {
        loadItems()
        return mutStockList
    }

    fun loadItems() {
        isLoading.value = true
        mutStockList = repository.allStock
        isLoading.value = false
        message.value = Event("updated")
    }

    fun getMessage(): LiveData<Event<String?>> {
        return message
    }

    fun getLoading(): LiveData<Boolean> {
        return isLoading
    }

    //for SearchView to find Name
    @RequiresApi(api = Build.VERSION_CODES.N)
    fun filterName(stockList: List<Stock>, text: String) {
        val resultList = stockList.stream()
            .filter { (name): Stock ->
                name.uppercase(Locale.getDefault()).startsWith(
                    text.uppercase(
                        Locale.getDefault()
                    )
                )
            }
            .collect(Collectors.toList())
        mutFilterNames.setValue(resultList)
    }

    fun filterNames(): LiveData<List<Stock>> = mutFilterNames

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun setContextualActionBarTitle(num: Int) {
        if (num > 1) {
            contextualActionBarTitle.setValue("$num Items Selected")
        } else {
            contextualActionBarTitle.setValue("$num Item Selected")
        }
    }

    fun getContextualActionBarTitle(): LiveData<String> {
        return contextualActionBarTitle
    }

    fun showAndHideRenameButton(num: Int) {
        isShowRenameButton.value = num < 2
    }

    fun isShowRenameButton(): LiveData<Boolean> {
        return isShowRenameButton
    }

    fun deleteStocks(selectStockIdList: List<Long?>, actionMode: ActionMode) {
        val disposable = repository.deleteStocksByIds(selectStockIdList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.setValue(Event<String?>("Deleted ${selectStockIdList.size} items"))
                actionMode.finish()
            }) { error: Throwable -> error.printStackTrace() }

        compositeDisposable.add(disposable)
    }

    fun isValidDelete(selectStockIdList: List<Long?>) {
        if (!selectStockIdList.isEmpty()) {
            isValidDelete.setValue(true)
        } else {
            isValidDelete.setValue(false)
            message.setValue(Event<String?>("Please select item"))
        }
    }

    fun getIsValidDelete(): LiveData<Boolean> {
        return isValidDelete
    }
}