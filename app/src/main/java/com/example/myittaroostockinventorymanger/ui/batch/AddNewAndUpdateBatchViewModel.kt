package com.example.myittaroostockinventorymanger.ui.batch

import android.os.Build
import android.util.Log
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.myittaroostockinventorymanger.event.Event
import com.example.myittaroostockinventorymanger.data.entities.Batch
import com.example.myittaroostockinventorymanger.data.entities.BatchWithItem
import com.example.myittaroostockinventorymanger.data.entities.Item
import com.example.myittaroostockinventorymanger.data.entities.Transaction
import com.example.myittaroostockinventorymanger.data.repository.Repository
import com.example.myittaroostockinventorymanger.enum.Option
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalTime
import java.util.*
import kotlin.math.E

class AddNewAndUpdateBatchViewModel : ViewModel() {

    private var repository: Repository =
        Repository()
    private var mutItemNames: LiveData<List<String>>? = null
    private var mutErrorMessage: MutableLiveData<Event<String>> = MutableLiveData()
    private var returnBack: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val option: MutableLiveData<String> = MutableLiveData()
    private val batchIdLiveData: MutableLiveData<Long> = MutableLiveData()
    private val isValidBatch: MutableLiveData<Event<Batch>> = MutableLiveData()

    //Use switchMap when batchIdLiveData update switch return new LiveData related type
    var existingBatch: LiveData<BatchWithItem> = batchIdLiveData.switchMap {
        repository.findBatchWithItemByIds(it)
    }


    fun getAllItemNames(): LiveData<List<String>>? {

        if (mutItemNames == null) {
            mutItemNames = MutableLiveData()
            mutItemNames = repository.allItemNames
        }
        return mutItemNames
    }

    //to save and update in local database
    fun onClickSave(
        option: String, updateBatchId: Long,
        actItemName: EditText, edtExpDate: EditText,
        edtQuantity: EditText, edtBasePrice: EditText,
        edtSellingPrice: EditText, edtDate: EditText
    ) {

        Log.d("myTag", "onClickSave: ${edtDate.text.length}")

        if (isValidInput(actItemName, edtQuantity, edtBasePrice, edtSellingPrice, edtDate)) {
            val batch =
                createBatch(actItemName, edtExpDate, edtQuantity, edtBasePrice, edtSellingPrice)

            val disposable = repository.findItemIdByName(actItemName.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    batch.itemId = it

                    isValidBatch.value = Event(
                        when (option) {
                            Option.NEW_ITEM.name -> batch


                            else -> {
                                batch.batchId = updateBatchId
                                batch
                            }
                        }
                    )

                }) {
                    Log.d("testTag", "insertBatch:  ")
                }
            compositeDisposable.add(disposable)
        }
    }

    fun getIsValidBatch(): LiveData<Event<Batch>> = isValidBatch

    private fun insertTransaction(transaction: Transaction) {
        val disposable = repository.insertTransaction(transaction)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mutErrorMessage.value = Event("Saved Transaction")
            }) {
                mutErrorMessage.value = Event("Can't Save")
            }
        compositeDisposable.add(disposable)
    }

    //find Transaction with batch id and update
    private fun updateTransactionNow(batch: Batch) {

        val disposable = repository.findTransactionByBatchId(batch.batchId)
            .flatMapCompletable {
                val transactionList = it.filter { it.itemIn > 0 }
                val updateTransaction = transactionList[0]
                    .apply {
                        date = Date(System.currentTimeMillis())
                        itemIn = batch.quantity
                    }
                repository.updateTransaction(updateTransaction)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mutErrorMessage.value = Event("Updated Transaction")
            }) {
                mutErrorMessage.value = Event("Can't Update")

            }

        compositeDisposable.add(disposable)
    }

    fun insertBatch(batch: Batch) {
        val disposable = repository.insertBatch(batch)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //add transaction
                insertTransaction(createTransaction(it, batch))
                mutErrorMessage.value = Event("Save")
                returnBack.value = Event(true)
            }) {
                mutErrorMessage.value = Event("Cannot Save")
            }
        compositeDisposable.add(disposable)
    }

    //create Transaction
    private fun createTransaction(batchId: Long, batch: Batch): Transaction {
        val date = Date(System.currentTimeMillis())
        return Transaction(
            batchId,
            itemIn = batch.quantity,
            itemOut = 0,
            profit = 0.0,
            date
        )
    }

    fun updateBatch(batch: Batch) {
        val disposable = repository.updateBatch(batch)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //update transaction
                updateTransactionNow(batch)
                mutErrorMessage.value = Event("Save")
                returnBack.value = Event(true)
            }) {
                mutErrorMessage.value = Event("Cannot Save")
            }
        compositeDisposable.add(disposable)
    }


    fun checkInsertOrUpdateBatch(batchId: Long) {

        if (batchId > 0) {
            option.value = Option.UPDATE_ITEM.name
        } else {
            option.value = Option.NEW_ITEM.name
        }
    }

    fun getOption(): LiveData<String> = option

    fun findBatchWithItemById(batchId: Long) {
        batchIdLiveData.value = batchId
    }

    //to check the user input is valid or not
    private fun isValidInput(
        edtStockName: EditText,
        edtAmount: EditText, edtCostPrice: EditText,
        edtSalePrice: EditText, edtDate: EditText
    ): Boolean {

        var isNameValid: Boolean
        var isDateValid: Boolean
        var isAmountValid: Boolean
        var isCostPriceValid: Boolean
        var isSalePriceValid: Boolean
        var errorMessage = ""

        //check stock name is valid
        if (edtStockName.text.isNotEmpty()) {
            isNameValid = true
        } else {
            isNameValid = false
            errorMessage += "Please fill the name\n"
        }

        //check amount is valid
        if (edtAmount.text.isNotEmpty()) {
            var amount = edtAmount.text.toString().toInt()
            if (amount > 0) {
                isAmountValid = true
            } else {
                isAmountValid = false
                errorMessage += "Amount cannot be less than 0\n"
            }
        } else {
            isAmountValid = false
            errorMessage += "Amount is empty\n"
        }

        //check costPrice is valid
        if (edtCostPrice.text.isNotEmpty()) {
            var costPrice = edtCostPrice.text.toString().toDouble()
            if (costPrice > 0) {
                isCostPriceValid = true
            } else {
                isCostPriceValid = false
                errorMessage += "Cost price cannot be less than 0\n"
            }

        } else {
            isCostPriceValid = false
            errorMessage += "Cost price is empty\n"
        }

        //check salePrice is valid
        if (edtSalePrice.text.isNotEmpty()) {
            var salePrice = edtSalePrice.text.toString().toDouble()
            if (salePrice > 0) {
                isSalePriceValid = true
            } else {
                isSalePriceValid = false
                errorMessage += "Sale price cannot be less than 0\n"
            }

        } else {
            isSalePriceValid = false
            errorMessage += "Sale price is empty\n"
        }

        //check date is valid
        if (edtDate.text.length != 10) {
            isDateValid = false
            errorMessage += "Invalid date\n"
        } else {
            isDateValid = true
        }


        return if (isNameValid && isAmountValid && isCostPriceValid && isSalePriceValid && isDateValid) {
            true
        } else {
            mutErrorMessage.value = Event(errorMessage)
            false
        }

    }

    private fun createBatch(
        edtStockName: EditText, edtExpDate: EditText,
        edtAmount: EditText, edtCostPrice: EditText,
        edtSalePrice: EditText
    ): Batch {

        val stockName = edtStockName.text.toString()
        var expDate: Date
        val format = SimpleDateFormat("dd/MM/yyyy")
        expDate = format.parse(edtExpDate.text.toString())!!
        val amount = edtAmount.text.toString().toInt()
        val costPrice = edtCostPrice.text.toString().toDouble()
        val salePrice = edtSalePrice.text.toString().toDouble()

        return Batch(0, costPrice, salePrice, amount, expDate);

    }

    fun findItemByName(itemName: String): LiveData<Item> {
        return repository.findItemByName(itemName)
    }

    fun getMessage(): LiveData<Event<String>> {
        return mutErrorMessage
    }

    fun isReturnBack(): LiveData<Event<Boolean>> {
        return returnBack
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}