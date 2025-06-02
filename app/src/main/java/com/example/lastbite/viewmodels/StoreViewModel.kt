package com.example.lastbite.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.util.LruCache
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lastbite.SessionManager
import com.example.lastbite.models.Store
import com.example.lastbite.models.Zone
import com.example.lastbite.repositories.StoreRepository

class StoreViewModel : ViewModel() {

    private val repository = StoreRepository()

    private val _stores = MutableLiveData<List<Store>>()
    val stores: LiveData<List<Store>> get() = _stores

    private val _storesByUser = MutableLiveData<List<Store>>()
    val storesByUser: LiveData<List<Store>> get() = _storesByUser

    private val _nearByStores = MutableLiveData<List<Store>>()
    val nearByStores: LiveData<List<Store>> get() = _nearByStores

    private val storesCache = object : LruCache<Int, List<Store>>(8) {}

    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun fetchStoresByIds(storeIds: List<Int>) {
        val cached = storesCache[SessionManager.getUser()!!.user_id]
        if (cached != null) {
            _storesByUser.postValue(cached)
        } else {
            repository.fetchStoresByIds(storeIds) { storeList ->
                Log.d("StoreVM", "The store IDs fetched are the following: $storeIds")
                _storesByUser.postValue(storeList ?: emptyList()) // Si no hay tiendas, mandamos lista vacía
                storesCache.put(SessionManager.getUser()!!.user_id, storeList)
            }
        }
    }

    fun loadStores() {
        repository.fetchStores { storeList -> 
            Log.d("StoreVM", "The total of fetched stores is ${storeList?.size}.")
            _stores.postValue(storeList ?: emptyList()) // Si es null, manda una lista vacía
        }
    }

    fun loadNearByStores(latitude: Double, longitude: Double) {
        repository.fetchNearbyStores(latitude, longitude) { storeList ->
            Log.d("StoreVM", "The total of fetched stores is ${storeList?.size}.")
            _nearByStores.postValue(storeList ?: emptyList()) // Si es null, manda una lista vacía
        }
    }

    fun createStore(store: Store) {
        repository.createStore(store, SessionManager.getUser()!!.user_id!!) { createdStore ->
            if (createdStore != null) {
                Log.d("StoreVM", "The store with ID ${createdStore.store_id} was generated.")
            } else {
                // Error al generar tienda
                Log.e("StoreVM", "There was an error generating the store.")
            }
        }
    }

    fun updateStore(storeId: Int, store: Store) {
        repository.updateStore(storeId, store) { updatedStore ->
            if (updatedStore != null) {
                Log.d("StoreVM", "The store with ID ${updatedStore.store_id} was updated.")
            } else {
                // Error al actualizar tienda
                Log.e("StoreVM", "There was an error updating the store.")
            }
        }
    }
}