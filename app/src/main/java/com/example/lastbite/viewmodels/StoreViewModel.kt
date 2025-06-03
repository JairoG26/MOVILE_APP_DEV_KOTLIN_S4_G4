package com.example.lastbite.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.util.LruCache
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lastbite.SessionManager
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

    private val _store = MutableLiveData<Store?>()
    val store: LiveData<Store?> get() = _store

    private val storesCache = object : LruCache<Int, List<Store>>(8) {}

    fun loadStores() {
        repository.fetchStores { storeList -> 
            Log.d("DEBUG", "Stores recibidos: ${storeList?.size}")
            _stores.postValue(storeList ?: emptyList()) // Si es null, manda una lista vacía
        }
    }

    fun fetchStoresByIds(storeIds: List<Int>) {
        val cached = storesCache[SessionManager.getUser()!!.user_id]
        if (cached != null) {
            _storesByUser.postValue(cached)
        } else {
            repository.fetchStoresByIds(storeIds) { storeList ->
                Log.d("DEBUG", "storeIds: $storeIds")
                _storesByUser.postValue(storeList ?: emptyList()) // Si no hay tiendas, mandamos lista vacía
                storesCache.put(SessionManager.getUser()!!.user_id, storeList)
            }
        }
    }

    fun loadNearByStores(latitude: Double, longitude: Double) {
        repository.fetchNearbyStores(latitude, longitude) { storeList ->
            Log.d("DEBUG", "Stores recibidos: ${storeList?.size}")
            _nearByStores.postValue(storeList ?: emptyList()) // Si es null, manda una lista vacía
        }
    }

    fun createStore(store: Store) {
        repository.createStore(store, SessionManager.getUser()!!.user_id!!) { createdStore ->
            if (createdStore != null) {
                // Tienda creada con éxito
                Log.d("POST", "Tienda creada: ${createdStore.store_id}")
                // Puedes realizar acciones adicionales aquí si es necesario
            } else {
                // Error al crear tienda
                Log.e("POST", "Error al crear la tienda")
            }
        }
    }

    fun updateStore(storeId: Int, store: Store) {
        repository.updateStore(storeId, store) { updatedStore ->
            if (updatedStore != null) {
                // Tienda actualizada con éxito
                Log.d("PUT", "Tienda actualizada: ${updatedStore.store_id}")
                // Puedes realizar acciones adicionales aquí si es necesario
            } else {
                // Error al actualizar tienda
                Log.e("PUT", "Error al actualizar la tienda")
            }
        }
    }

    fun getStoreById(storeId: Int) {
        repository.getStoreById(storeId) { store ->
            if (store != null) {
                // Tienda encontrada
                Log.d("GET", "Tienda encontrada: ${store.store_id}")
                _store.postValue(store)
                // Puedes realizar acciones adicionales aquí si es necesario
            } else {
                // Tienda no encontrada
                Log.e("GET", "Tienda no encontrada")
            }
        }
    }

    fun hayConexion(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}