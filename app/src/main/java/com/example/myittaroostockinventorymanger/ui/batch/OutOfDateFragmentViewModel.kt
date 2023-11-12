package com.example.myittaroostockinventorymanger.ui.batch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.data.entities.BatchWithItem
import com.example.myittaroostockinventorymanger.data.repository.Repository

class OutOfDateFragmentViewModel : ViewModel() {

    private val repository = Repository()

    fun getOutOfDateBatchWithItem(date: Long): LiveData<List<BatchWithItem>> {
        return repository.getExpiredBatchWithItem(date)
    }
}