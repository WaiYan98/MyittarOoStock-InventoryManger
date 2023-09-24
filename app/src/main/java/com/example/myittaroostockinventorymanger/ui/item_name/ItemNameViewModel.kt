package com.example.myittaroostockinventorymanger.ui.item_name

import android.os.Build
import android.util.Log
import android.view.ActionMode
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.data.entities.Item
import com.example.myittaroostockinventorymanger.data.repository.Repository
import com.example.myittaroostockinventorymanger.event.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Locale
import java.util.stream.Collectors

class ItemNameViewModel : ViewModel() {
    private var mutItemList: LiveData<List<Item>>
    private var searchItems: LiveData<List<Item>>
    private val message: MutableLiveData<Event<String?>>
    private val isLoading: MutableLiveData<Boolean>
    private val mutFilterNames: MutableLiveData<List<Item>>
    private val contextualActionBarTitle: MutableLiveData<String>
    private val repository: Repository
    private val compositeDisposable = CompositeDisposable()
    private val isShowRenameButton: MutableLiveData<Boolean>
    private val isValidDelete: MutableLiveData<Boolean>

    init {
        mutItemList = MutableLiveData()
        repository = Repository()
        message = MutableLiveData()
        isLoading = MutableLiveData(false)
        mutFilterNames = MutableLiveData()
        contextualActionBarTitle = MutableLiveData()
        isShowRenameButton = MutableLiveData()
        isValidDelete = MutableLiveData()
        searchItems = MutableLiveData()
        loadItems()
    }

    fun getAllItems(): LiveData<List<Item>> {
        return mutItemList
    }

    fun loadItems() {
        isLoading.value = true
        mutItemList = repository.allItem
        isLoading.value = false
        message.value = Event("updated")
    }

    fun getMessage(): LiveData<Event<String?>> {
        return message
    }

    fun getLoading(): LiveData<Boolean> {
        return isLoading
    }


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

    fun deleteStocks(selectStockIdList: List<Long>, actionMode: ActionMode) {
        val disposable = repository.deleteItemsById(selectStockIdList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.value = Event<String?>("Deleted ${selectStockIdList.size} items")
                actionMode.finish()
            }) { error: Throwable -> error.printStackTrace() }

        compositeDisposable.add(disposable)
    }

    fun isValidDelete(selectStockIdList: List<Long?>) {
        if (!selectStockIdList.isEmpty()) {
            isValidDelete.setValue(true)
        } else {
            isValidDelete.value = false
            message.setValue(Event<String?>("Please select item"))
        }
    }

    fun getIsValidDelete(): LiveData<Boolean> {
        return isValidDelete
    }

    // TODO: repair and check for issue
    fun searchItemsFromDb(queryText: String): LiveData<List<Item>> {
        Log.d("tag", "searchItemsFromDb: $queryText")
        return repository.searchItems(queryText)
    }

    fun getSearchItems() = searchItems
}