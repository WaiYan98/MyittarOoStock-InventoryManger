package com.example.myittaroostockinventorymanger.ui.item_name

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.data.entities.Item
import com.example.myittaroostockinventorymanger.data.repository.Repository
import com.example.myittaroostockinventorymanger.event.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddAndUpdateItemViewModel : ViewModel() {
    private val repository: Repository = Repository()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val isValidItem: MutableLiveData<Event<Item>> = MutableLiveData()
    private val message: MutableLiveData<Event<String>> = MutableLiveData()
    private var item: LiveData<Item> = MutableLiveData()

    fun insertItem(item: Item) {
        val disposable = repository.insertItem(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message.setValue(Event("Added")) }) { error: Throwable ->
                message.setValue(
                    Event("unexpected error")
                )
            }
        compositeDisposable.add(disposable)
    }

    fun updateItemName(item: Item) {
        val disposable = repository.updateItemName(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message.setValue(Event("Renamed")) }) { error: Throwable? ->
                message.setValue(
                    Event("Cannot Renamed")
                )
            }
        compositeDisposable.add(disposable)
    }

    fun findItemById(id: Long) {
        item = repository.getItemById(id)
    }

    //Check the item is empty or not
    fun onClickDone(item: Item) {
        val itemName = item.name
        if (!TextUtils.isEmpty(itemName)) {
            isValidItem.setValue(Event(item))
        } else {
            message.setValue(Event("Empty name cannot be saved"))
        }
    }

    val getIsValidItem: LiveData<Event<Item>>
        //get the check stock to update or insert
        get() = isValidItem

    fun getMessage(): LiveData<Event<String>> {
        return message
    }

    fun getItem() = item

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}