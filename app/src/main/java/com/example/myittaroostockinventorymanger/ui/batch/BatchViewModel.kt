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

    private val repository: Repository = Repository()
    private var BatchWithItem: LiveData<List<BatchWithItem>>? = null
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
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