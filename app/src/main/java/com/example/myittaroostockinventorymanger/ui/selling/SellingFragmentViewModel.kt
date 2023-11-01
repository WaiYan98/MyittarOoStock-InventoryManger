package com.example.myittaroostockinventorymanger.ui.selling

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.myittaroostockinventorymanger.data.entities.Batch
import com.example.myittaroostockinventorymanger.data.entities.Item
import com.example.myittaroostockinventorymanger.data.entities.Transaction
import com.example.myittaroostockinventorymanger.data.repository.Repository
import com.example.myittaroostockinventorymanger.event.Event
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Date

class SellingFragmentViewModel : ViewModel() {

    private var sellingItemNameList: LiveData<List<String>> = MutableLiveData()
    private val repository = Repository()
    private val mutItemId: MutableLiveData<Long> = MutableLiveData()
    private val message: MutableLiveData<Event<String>> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var isValidBatch: MutableLiveData<Event<Batch>> = MutableLiveData()
    private val isValidTransaction: MutableLiveData<Event<Transaction>> = MutableLiveData()
    private val isReturnBack: MutableLiveData<Event<Boolean>> = MutableLiveData()


    private val sellingBatchList: LiveData<List<Batch>> = mutItemId.switchMap {
        repository.findBatchByItemId(it)
    }


    fun findSellingItemNames() {
        sellingItemNameList = repository.getAllItemIds()
            .switchMap {
                repository.findItemNameByIds(it)
            }
    }

    fun getSellingItemNameList() = sellingItemNameList
    fun getItemByNames(itemName: String): LiveData<Item> {
        return repository.findItemByName(itemName)
    }

    fun findBatchByItemId(itemId: Long) {
        mutItemId.value = itemId
    }

    fun getValidBatch(): LiveData<Event<Batch>> = isValidBatch

    fun getValidTransaction(): LiveData<Event<Transaction>> = isValidTransaction

    private fun findBatchById(batchId: Long): Single<Batch> {
        return repository.findBatchByBatchId(batchId)
    }

    fun getIsReturnBack(): LiveData<Event<Boolean>> = isReturnBack

    fun updateBatch(batch: Batch, qty: Int) {
        val disposable = repository.updateBatch(batch)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.value = Event("Selling Done")
                //transaction add
                isValidTransaction.value = Event(createTransaction(batch, qty))
            }) {
                message.value = Event(it.message ?: "UnexpectedError")
            }
        compositeDisposable.add(disposable)
    }

    fun getMessage(): LiveData<Event<String>> = message

    fun getSellingBatchList() = sellingBatchList

    fun insertTransaction(transaction: Transaction) {

        val disposable = repository.insertTransaction(transaction)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.value = Event("Transaction In Added")
                isReturnBack.value = Event(true)
            }) {
                message.value = Event(it.message)
            }

        compositeDisposable.add(disposable)
    }

    private fun createTransaction(batch: Batch, qty: Int): Transaction {
        val currentDate = Date(System.currentTimeMillis())
        return Transaction(
            batch.batchId,
            itemIn = qty,
            itemOut = 0,
            profit = qty * (batch.sellingPrice - batch.basePrice),
            currentDate
        )
    }

    //Check if Qty is less than ExistQty return true
    private fun isValidSellingQty(qty: Int, existQty: Int): Boolean {
        return existQty >= qty
    }


    // TODO: ask
    fun onClickSale(sellingBatchId: Long, qty: Int) {

        if (sellingBatchId.toInt() != 0 && qty != 0) {

            val disposable = findBatchById(sellingBatchId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    if (isValidSellingQty(qty, it.quantity)) {
                        Log.d("Single", "onClickSale: $it")
                        it.apply { quantity -= qty }
                        isValidBatch.value = Event(it)
                    } else {
                        message.value = Event("Not Enough Item For Selling")
                    }

                }) {
                    message.value = Event(it.message ?: "Unexpected Error")
                }

            compositeDisposable.add(disposable)

        } else {
            message.value = Event(
                when (0) {
                    sellingBatchId.toInt() -> "Please Select Batch"
                    qty -> "Please Enter the Quantity"
                    else -> "Unexpected Error"
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}