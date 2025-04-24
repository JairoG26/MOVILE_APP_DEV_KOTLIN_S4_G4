package com.example.lastbite.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lastbite.ApiService
import com.example.lastbite.models.UserStore
import com.example.lastbite.repositories.UserStoreRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserStoreViewModel : ViewModel() {

    private val _storeIds = MutableLiveData<List<Int>>()
    val storeIds: LiveData<List<Int>> = _storeIds

    private val repository = UserStoreRepository()

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchStoreIdsByUser(userId: Int?) {
        repository.getUserStoresByUserId(userId) { storeIds, errorMsg ->
            if (storeIds != null) {
                _storeIds.value = storeIds!!
                _error.value = null
            } else {
                _error.value = errorMsg
            }
        }
    }
}