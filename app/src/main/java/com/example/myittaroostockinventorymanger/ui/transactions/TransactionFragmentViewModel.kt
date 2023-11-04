package com.example.myittaroostockinventorymanger.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.myittaroostockinventorymanger.data.entities.Transaction
import com.example.myittaroostockinventorymanger.data.repository.Repository

class TransactionFragmentViewModel : ViewModel() {

    private val mutBatchIdList: MutableLiveData<List<Long>> = MutableLiveData()
    private val repository = Repository()

    val batchWithItemList = mutBatchIdList
        .switchMap {
            repository.findBatchWithItemByBatchIds(it)
        }


    fun getAllTransaction(): LiveData<List<Transaction>> {
        return repository.getAllTransaction()
    }

    fun setBatchIdList(batchIdList: List<Long>) {
        mutBatchIdList.value = batchIdList
    }
}