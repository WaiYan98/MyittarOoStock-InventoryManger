package com.example.myittaroostockinventorymanger.ui.batch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.data.entities.BatchWithItem
import com.example.myittaroostockinventorymanger.data.repository.Repository

class OutOfStockFragmentViewModel :ViewModel() {

    private val repository = Repository()

    fun getOutOfStockBatchWithItem(num :Int) :LiveData<List<BatchWithItem>>{
        return repository.getOutOfStockBatchWithItem(num)
    }


}