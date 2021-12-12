package com.example.myittaroostockinventorymanger.batch_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.local.StockWithBatch
import com.example.myittaroostockinventorymanger.repository.Repository

class BatchViewModel : ViewModel() {

    private val repository: Repository = Repository()
    private var stockWithBatches: LiveData<List<StockWithBatch>>? = null

    fun getAllStockWithBatches(): LiveData<List<StockWithBatch>>? {

        if (stockWithBatches == null) {
            stockWithBatches = MutableLiveData()
            stockWithBatches = repository.allStockWithBatch
        }
        return stockWithBatches
    }

}