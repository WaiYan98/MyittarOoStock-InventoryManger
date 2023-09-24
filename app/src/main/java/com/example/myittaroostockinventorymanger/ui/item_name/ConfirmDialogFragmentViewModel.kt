package com.example.myittaroostockinventorymanger.ui.item_name

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.data.repository.Repository
import com.example.myittaroostockinventorymanger.event.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ConfirmDialogFragmentViewModel : ViewModel() {

    private val message = MutableLiveData<Event<String>>()
    private val numOfDeleteItem = MutableLiveData<String>()
    var repository = Repository()
    private lateinit var disposable: Disposable
    fun checkIsValidDelete(idList: List<Long>) {
        if (idList.isNotEmpty()) {
            deleteItems(idList)
        } else {
            message.setValue(Event("Please Select Item"))
        }
    }

    private fun deleteItems(idList: List<Long>) {
        disposable = repository.deleteItemsById(idList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message.setValue(Event("Deleted")) }) { error: Throwable? ->
                message.setValue(
                    Event("Cannot Delete")
                )
            }
    }

    fun numOfDeleteItems(num: Int) {
        return if (num == 1) {
            numOfDeleteItem.value = "Delete $num Item"
        } else {
            numOfDeleteItem.value = "Delete $num Items"
        }
    }

    fun getMessage() = message

    fun getNumOfDeleteItem() = numOfDeleteItem
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}