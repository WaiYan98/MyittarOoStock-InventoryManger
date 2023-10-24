package com.example.myittaroostockinventorymanger.ui.item_name

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.example.myittaroostockinventorymanger.data.entities.BatchWithItem
import com.example.myittaroostockinventorymanger.data.repository.Repository
import com.example.myittaroostockinventorymanger.event.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ConfirmDialogFragmentViewModel : ViewModel() {

    private val message = MutableLiveData<Event<String>>()
    private val numOfDeleteItem = MutableLiveData<String>()
    var repository = Repository()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val isItemRelatedToBatch: MutableLiveData<Boolean> = MutableLiveData()
    private val itemIdsRelatedToBatch: MutableLiveData<List<Long>> = MutableLiveData()


    fun checkIsValidDelete(idList: List<Long>, existingIds: List<Long>) {
        if (idList.isNotEmpty()) {
            val idsRelatedToBatch = idList.filter { existingIds.contains(it) }
            isItemRelatedToBatch.value = idsRelatedToBatch.isNotEmpty()
            Log.d("testtest", "checkIsValidDelete: ${idsRelatedToBatch.toString()}")
        } else {
            message.setValue(Event("Please Select Item"))
        }
    }

    //If Item have related to batch data => update the LiveData value
    fun checkIsItemRelatedToBatch(ids: List<Long>, existingIds: List<Long>) {

        val itemIdsRelatedToBatch = ids.filter { existingIds.contains(it) }

        if (itemIdsRelatedToBatch.isNotEmpty()) {
            this.itemIdsRelatedToBatch.value = itemIdsRelatedToBatch
        }
    }

    fun deleteBatchesByItemIds(ids: List<Long>) {
        val disposable = repository.deleteBatchesByItemIds(ids)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.value = Event("We deleted all related batches")
            }) {
                message.value = Event("Can't Delete")
            }

        compositeDisposable.add(disposable)
    }

    fun getBatchWithItemByItemIds(ids: List<Long>): LiveData<List<BatchWithItem>> {
        return repository.findBatchWithItemByIds(ids)
    }

    fun getAllExistItemIdFromBatch() = repository.getAllItemIds()

    fun deleteItems(idList: List<Long>) {
        val disposable = repository.deleteItemsById(idList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.setValue(Event("Deleted"))
            }) { error: Throwable? ->
                message.setValue(
                    Event("Cannot Delete")
                )
            }
        compositeDisposable.add(disposable)
    }

    fun numOfDeleteItems(num: Int) {
        return if (num == 1) {
            numOfDeleteItem.value = "Delete $num Item"
        } else {
            numOfDeleteItem.value = "Delete $num Items"
        }
    }

    fun getMessage() = message

    val getIsItemRelatedToBatch: LiveData<Boolean>
        get() = isItemRelatedToBatch

    val getItemIdsRelatedToBatch: LiveData<List<Long>>
        get() = itemIdsRelatedToBatch

    fun getNumOfDeleteItem() = numOfDeleteItem
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}