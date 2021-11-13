package com.example.myittaroostockinventorymanger.batch_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myittaroostockinventorymanger.repository.Repository

class BatchViewModel : ViewModel() {

    private val repository: Repository = Repository()
    private var navigateToAddNewBatchActivity: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

}