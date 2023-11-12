package com.example.myittaroostockinventorymanger.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.switchMap
import com.example.myittaroostockinventorymanger.data.entities.Transaction
import com.example.myittaroostockinventorymanger.data.repository.Repository
import kotlinx.coroutines.flow.filter

class TransactionFragmentViewModel : ViewModel() {

    private val repository = Repository()
    private val sortedTransaction: MutableLiveData<List<Transaction>> = MutableLiveData()

    fun getAllTransaction(): LiveData<List<Transaction>> {
        return repository.getAllTransaction()

    }

    fun getSortedTransaction(): LiveData<List<Transaction>> = sortedTransaction


    //todo repair sorted fun
    //sometime issues not sort after configuration change
    fun sortedTransaction(transactionList: List<Transaction>) {
        sortedTransaction.value =
            transactionList.sortedWith { transaction1, transaction2 ->
                return@sortedWith transaction2.date.compareTo(transaction1.date)
            }

    }

}