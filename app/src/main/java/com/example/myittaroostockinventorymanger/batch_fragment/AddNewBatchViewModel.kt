package com.example.myittaroostockinventorymanger.batch_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.repository.Repository

class AddNewBatchViewModel : ViewModel() {

    private var repository: Repository = Repository()
    private var mutStockNames: LiveData<List<String>>? = null

    fun getAllStockNames(): LiveData<List<String>>? {

        if (mutStockNames == null) {
            mutStockNames = MutableLiveData()
            mutStockNames = repository.allStockNames
        }
        return mutStockNames
    }

}