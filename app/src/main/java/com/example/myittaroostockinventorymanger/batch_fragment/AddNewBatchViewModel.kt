package com.example.myittaroostockinventorymanger.batch_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.event.Event
import com.example.myittaroostockinventorymanger.local.Stock
import com.example.myittaroostockinventorymanger.local.StockWithBatch
import com.example.myittaroostockinventorymanger.repository.Repository

class AddNewBatchViewModel : ViewModel() {

    private var repository: Repository = Repository()
    private var mutStockWithBatches: MutableLiveData<List<StockWithBatch>> = MutableLiveData<List<StockWithBatch>>()
    private var mutStockNames: LiveData<List<String>>? = null

    fun getAllStockNames(): LiveData<List<String>>? {

        if (mutStockNames == null) {
            mutStockNames = MutableLiveData()
            mutStockNames = repository.allStockNames
        }
        return mutStockNames
    }


}