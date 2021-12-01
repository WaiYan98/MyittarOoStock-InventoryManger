package com.example.myittaroostockinventorymanger.batch_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.event.Event
import com.example.myittaroostockinventorymanger.local.Batch
import com.example.myittaroostockinventorymanger.repository.Repository
import java.util.*

class AddNewBatchViewModel : ViewModel() {

    private var repository: Repository = Repository()
    private var mutStockNames: LiveData<List<String>>? = null
    private var mutErrorMessage: MutableLiveData<Event<String>> = MutableLiveData()


    fun getAllStockNames(): LiveData<List<String>>? {

        if (mutStockNames == null) {
            mutStockNames = MutableLiveData()
            mutStockNames = repository.allStockNames
        }
        return mutStockNames
    }

    fun onClickSave(batch: Batch, isValidDate: Boolean, itemName: String) {

        if (itemName.isNotEmpty() && isValidDate && batch.totalStock > 0) {
            var stockId =
            var batch  = Batch()
            repository.insertBatch()
        }

    }


}