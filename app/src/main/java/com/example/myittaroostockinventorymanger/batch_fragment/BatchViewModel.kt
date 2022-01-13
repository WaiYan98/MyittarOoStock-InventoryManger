package com.example.myittaroostockinventorymanger.batch_fragment

import android.view.ActionMode
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.event.Event
import com.example.myittaroostockinventorymanger.local.StockWithBatch
import com.example.myittaroostockinventorymanger.pojo.StockBatch
import com.example.myittaroostockinventorymanger.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers.io

class BatchViewModel : ViewModel() {

    private val repository: Repository = Repository()
    private var stockWithBatches: LiveData<List<StockWithBatch>>? = null
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var searchBatchResultList: MutableLiveData<List<StockBatch>> = MutableLiveData()
    private var message: MutableLiveData<Event<String>> = MutableLiveData()
    private var contextualTitle: MutableLiveData<String> = MutableLiveData()
    private var showRenameButton: MutableLiveData<Boolean> = MutableLiveData()

    fun getAllStockWithBatches(): LiveData<List<StockWithBatch>>? {

        if (stockWithBatches == null) {
            stockWithBatches = MutableLiveData()
            loadStockWithBatches()
        }
        return stockWithBatches
    }

    fun loadStockWithBatches() {
        isLoading.value = true
        stockWithBatches = repository.allStockWithBatch
        isLoading.value = false
        message.value = Event("updated")
    }

    fun searchBatchByName(stockBatchList: List<StockBatch>, newText: String) {

        val resultList = stockBatchList.filter { it.stock.name.startsWith(newText, ignoreCase = true) }

        searchBatchResultList.value = resultList
    }

//    //this should implement in confirmDialog
//    fun deleteBatchById(id: Long) {
//        val disposable = repository.deleteBatchById(id)
//                .subscribeOn(io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    message.value = Event("deleted")
//                }) {
//                    it.printStackTrace()
//                }
//    }

    fun setContextualActionBarTitle(num: Int) {
        if (num < 2) {
            contextualTitle.value = "$num Item Selected"
        } else {
            contextualTitle.value = "$num Items Selected"
        }
    }

    fun getContextualActionBarTitle() = contextualTitle

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun getMessage(): LiveData<Event<String>> {
        return message
    }

    fun getSearchResult(): LiveData<List<StockBatch>> {
        return searchBatchResultList
    }

    fun deleteBatches(batchesIdList: List<Long>, actionMode: ActionMode) {
        val disposable = repository.deleteBatchesById(batchesIdList)
                .subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    message.value = Event("Delete ${batchesIdList.size} Items ")
                    actionMode.finish()
                }) {
                    it.printStackTrace()
                }
    }

    fun showRenameButton(num: Int) {
        showRenameButton.value = num < 2
    }

    fun isShowRenameButton() = showRenameButton

    override fun onCleared() {
        super.onCleared()
    }

}