package com.example.lastbite.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lastbite.models.Store
import com.example.lastbite.repositories.StoreRepository
import kotlinx.coroutines.launch


class StoreViewModel(private val storeRepository: StoreRepository) : ViewModel() {
    private val _stores = MutableLiveData<List<Store>>()
    val stores: LiveData<List<Store>> get() = _stores

    fun getStores() {
        viewModelScope.launch {
            try {
                _stores.value = storeRepository.fetchStores()
            } catch (e: Exception) {
                Log.e("StoreViewModel", "Error loading stores", e)
            }
        }
    }
}