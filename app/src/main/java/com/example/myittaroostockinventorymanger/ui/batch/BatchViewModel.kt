package com.example.myittaroostockinventorymanger.ui.batch

import android.view.ActionMode
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.myittaroostockinventorymanger.data.entities.Batch
import com.example.myittaroostockinventorymanger.data.entities.BatchWithItem
import com.example.myittaroostockinventorymanger.event.Event
import com.example.myittaroostockinventorymanger.data.entities.ItemBatch
import com.example.myittaroostockinventorymanger.data.repository.Repository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io

class BatchViewModel : ViewModel() {

    private val repository: Repository =
        Repository()
    private var BatchWithItem: LiveData<List<BatchWithItem>>? = null
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var searchBatchResultList: MutableLiveData<List<ItemBatch>> = MutableLiveData()
    private var message: MutableLiveData<Event<String>> = MutableLiveData()
    private var contextualTitle: MutableLiveData<String> = MutableLiveData()
    private var showRenameButton: MutableLiveData<Boolean> = MutableLiveData()

    fun getAllBatchWithItem(): LiveData<List<BatchWithItem>>? {

        if (BatchWithItem == null) {
            BatchWithItem = MutableLiveData()
            loadStockWithBatches()
        }
        return BatchWithItem
    }

    fun loadStockWithBatches() {
        isLoading.value = true
        BatchWithItem = repository.allBatchWithItem
        isLoading.value = false
        message.value = Event("updated")
    }

    //Search view QueryText search from db
    fun searchFromDb(queryText: String): LiveData<List<BatchWithItem>> {
        return repository.findIdListByQueryText(queryText)
            .switchMap {
                repository.findBatchWithItemByIds(it)
            }
    }

    fun searchBatchByName(itemBatchList: List<ItemBatch>, newText: String) {

        val resultList =
            itemBatchList.filter { it.item.name.startsWith(newText, ignoreCase = true) }

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

    fun getSearchResult(): LiveData<List<ItemBatch>> {
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

    //for test
    fun insertBatch(batch: Batch): Completable {
        return repository.insertBatch(batch)
    }

}