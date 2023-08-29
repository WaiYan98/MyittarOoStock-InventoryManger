package com.example.myittaroostockinventorymanger.ui.batch

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.event.Event
import com.example.myittaroostockinventorymanger.data.entities.Batch
import com.example.myittaroostockinventorymanger.data.repository.Repository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import java.text.SimpleDateFormat
import java.util.*

class AddNewAndUpdateBatchViewModel : ViewModel() {

    private var repository: Repository =
        Repository()
    private var mutStockNames: LiveData<List<String>>? = null
    private var mutErrorMessage: MutableLiveData<Event<String>> = MutableLiveData()
    private var navigateToMainActivity: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private lateinit var disposable: Disposable


    fun getAllStockNames(): LiveData<List<String>>? {

        if (mutStockNames == null) {
            mutStockNames = MutableLiveData()
            mutStockNames = repository.allStockNames
        }
        return mutStockNames
    }

    //to save and update in local database
    fun onClickSave(option: String, updateBatchId: Long,
                    edtStockName: EditText, edtExpDate: EditText,
                    edtAmount: EditText, edtCostPrice: EditText,
                    edtSalePrice: EditText, isValidDate: Boolean) {

        if (isValidInput(edtStockName, edtAmount, edtCostPrice, edtSalePrice, isValidDate)) {
            val batch = createBatch(edtStockName, edtExpDate, edtAmount, edtCostPrice, edtSalePrice)

            disposable = repository.findStockIdByName(edtStockName.text.toString())
                    .flatMap {
                        batch.stockId = it

                        Log.d("tag", "onClickSave: "+it)

                        if (option == BatchFragment.ADD_NEW) {
                            Log.d("tag", "onClickSave: " + batch)
                            repository.insertBatch(batch)
                        } else {
                            batch.batchId = updateBatchId
                            Log.d("tag", "onClickSave: " + batch)
                            repository.updateBatch(batch)
                        }.andThen(Observable.just(Unit))
                    }
                    .subscribeOn(io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mutErrorMessage.value = Event("Save")
                        navigateToMainActivity.value = Event(true)
                    }) {
                        mutErrorMessage.value = Event("Cannot Save")
                    }
        }
    }


    //to check the user input is valid or not
    private fun isValidInput(edtStockName: EditText,
                             edtAmount: EditText, edtCostPrice: EditText,
                             edtSalePrice: EditText, isValidDate: Boolean): Boolean {

        var isNameValid = false
        var isAmountValid = false
        var isCostPriceValid = false
        var isSalePriceValid = false
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

        if (!isValidDate) {
            errorMessage += "Invalid date\n"
        }

        return if (isNameValid && isAmountValid && isCostPriceValid && isSalePriceValid && isValidDate) {
            true
        } else {
            mutErrorMessage.value = Event(errorMessage)
            false
        }

    }

    private fun createBatch(edtStockName: EditText, edtExpDate: EditText,
                            edtAmount: EditText, edtCostPrice: EditText,
                            edtSalePrice: EditText): Batch {

        val stockName = edtStockName.text.toString()
        var expDate: Date
        val format = SimpleDateFormat("dd/MM/yyyy")
        expDate = format.parse(edtExpDate.text.toString())
        val amount = edtAmount.text.toString().toInt()
        val costPrice = edtCostPrice.text.toString().toDouble()
        val salePrice = edtSalePrice.text.toString().toDouble()

        return Batch(0, costPrice, salePrice, amount, expDate);

    }

    fun getMessage(): LiveData<Event<String>> {
        return mutErrorMessage
    }

    fun getNavigateToMainActivity(): LiveData<Event<Boolean>> {
        return navigateToMainActivity
    }

    override fun onCleared() {
        super.onCleared()
    }
}