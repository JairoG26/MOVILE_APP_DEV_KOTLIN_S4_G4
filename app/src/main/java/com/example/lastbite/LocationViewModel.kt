package com.example.lastbite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {
    val zones = MutableLiveData<List<String>>()
    val areas = MutableLiveData<List<String>>()
    val errorMessage = MutableLiveData<String>()

    fun fetchZones() {
        repository.getZones().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    zones.value = response.body()
                } else {
                    errorMessage.value = "Error al obtener zonas"
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                errorMessage.value = "Error de red: ${t.message}"
            }
        })
    }

    fun fetchAreas() {
        repository.getAreas().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    areas.value = response.body()
                } else {
                    errorMessage.value = "Error al obtener áreas"
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                errorMessage.value = "Error de red: ${t.message}"
            }
        })
    }
}