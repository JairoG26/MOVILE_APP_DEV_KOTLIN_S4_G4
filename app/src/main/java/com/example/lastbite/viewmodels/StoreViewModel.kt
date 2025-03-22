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


class StoreViewModel(private val storeRepository: StoreRepository) : ViewModel() {
    private val _stores = MutableLiveData<List<Store>>()
    val stores: LiveData<List<Store>> get() = _stores

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getStores() {
        storeRepository.fetchStores().enqueue(object : Callback<List<Store>> {
            override fun onResponse(call: Call<List<Store>>, response: Response<List<Store>>) {
                Log.d("FETCH_ZONES", "Código de respuesta: ${response.code()}")
                Log.d("FETCH_ZONES", "Cuerpo de respuesta: ${response.body()}")
                if (response.isSuccessful) {
                    _stores.value = response.body()
                } else {
                    _errorMessage.value = "Error al obtener zonas"
                }
            }

            override fun onFailure(call: Call<List<Store>>, t: Throwable) {
                _errorMessage.value = "Error de red: ${t.message}"
            }
        })
    }
}