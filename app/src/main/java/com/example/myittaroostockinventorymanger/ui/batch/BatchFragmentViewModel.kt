package com.example.myittaroostockinventorymanger.ui.batch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.data.repository.Repository

class BatchFragmentViewModel : ViewModel() {

    private val repository = Repository()
    fun getExpiredBatchQty(date: Long): LiveData<Long> {
        return repository.getExpiredBatchRowCount(date)
    }

    fun getOutOfStockBatchQty(num: Int): LiveData<Long> {
        return repository.getOutOfStockRowCount(num)
    }
}