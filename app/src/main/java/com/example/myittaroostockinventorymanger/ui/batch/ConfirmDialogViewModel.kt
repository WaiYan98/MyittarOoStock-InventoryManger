package com.example.myittaroostockinventorymanger.ui.batch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.data.repository.Repository
import com.example.myittaroostockinventorymanger.event.Event
import com.example.myittaroostockinventorymanger.util.Text
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ConfirmDialogViewModel : ViewModel() {

    private val title: MutableLiveData<String> = MutableLiveData()
    private val message: MutableLiveData<Event<String>> = MutableLiveData()
    private val repository = Repository()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private fun deleteBatches(ids: List<Long>) {
        val disposable = repository.deleteBatchesById(ids)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.value = Event("Deleted ${ids.size} ${Text.format(ids.size)}")
            }) {
                message.value = Event("Unexpected Error")
            }

        compositeDisposable.add(disposable)
    }

    fun setTitle(num: Int) {
        title.value = "Delete $num ${Text.format(num)}"
    }

    fun getTitle(): LiveData<String> = title
    fun onClickYes(selectedIdList: List<Long>) {
        if (selectedIdList.isNotEmpty()) {
            deleteBatches(selectedIdList)
        } else {
            message.value = Event("Please Select Item")
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun getMessage(): LiveData<Event<String>> = message

}