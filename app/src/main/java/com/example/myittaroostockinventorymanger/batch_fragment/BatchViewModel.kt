package com.example.myittaroostockinventorymanger.batch_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.event.Event
import com.example.myittaroostockinventorymanger.local.StockWithBatch
import com.example.myittaroostockinventorymanger.pojo.StockBatch
import com.example.myittaroostockinventorymanger.repository.Repository

class BatchViewModel : ViewModel() {

    private val repository: Repository = Repository()
    private var stockWithBatches: LiveData<List<StockWithBatch>>? = null
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var searchBatchResultList: MutableLiveData<List<StockBatch>> = MutableLiveData()
    private var message: MutableLiveData<Event<String>> = MutableLiveData()

    fun getAllStockWithBatches(): LiveData<List<StockWithBatch>>? {

        if (stockWithBatches == null) {
            stockWithBatches = MutableLiveData()
            loadStockWithBatches()
        }
        return stockWithBatches
    }

    fun loadStockWithBatches() {
        isLoading.value = true
        stockWithBatches = repository.allStockWithBatch
        isLoading.value = false
        message.value = Event("updated")
    }

    fun searchBatchByName(stockBatchList: List<StockBatch>, newText: String) {

        val resultList = stockBatchList.filter { it.stock.name.startsWith( newText, ignoreCase = true) }

        searchBatchResultList.value = resultList
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun getMessage(): LiveData<Event<String>> {
        return message
    }

    fun getSearchResult(): LiveData<List<StockBatch>> {
        return searchBatchResultList
    }


}