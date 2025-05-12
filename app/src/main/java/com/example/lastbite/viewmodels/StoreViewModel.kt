package com.example.lastbite.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lastbite.models.Store
import com.example.lastbite.models.Zone
import com.example.lastbite.repositories.StoreRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreViewModel : ViewModel() {
    private val repository = StoreRepository()

    private val _stores = MutableLiveData<List<Store>>()
    val stores: LiveData<List<Store>> get() = _stores

    private val _storesByUser = MutableLiveData<List<Store>>()
    val storesByUser: LiveData<List<Store>> get() = _storesByUser

    private val _nearByStores = MutableLiveData<List<Store>>()
    val nearByStores: LiveData<List<Store>> get() = _nearByStores

    fun loadStores() {
        repository.fetchStores { storeList -> 
            Log.d("DEBUG", "Stores recibidos: ${storeList?.size}")
            _stores.postValue(storeList ?: emptyList()) // Si es null, manda una lista vacía
        }
    }

    fun fetchStoresByIds(storeIds: List<Int>) {
        repository.fetchStoresByIds(storeIds) { storeList ->
            Log.d("DEBUG", "storeIds: $storeIds")
            _storesByUser.postValue(storeList ?: emptyList()) // Si no hay tiendas, mandamos lista vacía
        }
    }

    fun loadNearByStores(latitude: Double, longitude: Double) {
        repository.fetchNearbyStores(latitude, longitude) { storeList ->
            Log.d("DEBUG", "Stores recibidos: ${storeList?.size}")
            _nearByStores.postValue(storeList ?: emptyList()) // Si es null, manda una lista vacía
        }
    }

}