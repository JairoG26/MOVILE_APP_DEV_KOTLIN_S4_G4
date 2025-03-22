package com.example.lastbite.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lastbite.models.Area
import com.example.lastbite.models.Zone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.example.lastbite.repositories.LocationRepository

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    // LiveData para la lista de zonas y areas
    private val _zones = MutableLiveData<List<Zone>>()
    val zones: LiveData<List<Zone>> = _zones
    val areas = MutableLiveData<List<Area>>()
    val filteredAreas = MutableLiveData<List<Area>>()

    // LiveData para manejar errores
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // Método para obtener las zonas desde el repositorio
    fun fetchZones() {

        repository.getZones().enqueue(object : Callback<List<Zone>> {
            override fun onResponse(call: Call<List<Zone>>, response: Response<List<Zone>>) {
                Log.d("FETCH_ZONES", "Código de respuesta: ${response.code()}")
                Log.d("FETCH_ZONES", "Cuerpo de respuesta: ${response.body()}")
                if (response.isSuccessful) {
                    _zones.value = response.body()
                } else {
                    _errorMessage.value = "Error al obtener zonas"
                }
            }

            override fun onFailure(call: Call<List<Zone>>, t: Throwable) {
                _errorMessage.value = "Error de red: ${t.message}"
            }
        })
    }

    fun fetchAreas() {
        repository.getAreas().enqueue(object : Callback<List<Area>> {
            override fun onResponse(call: Call<List<Area>>, response: Response<List<Area>>) {
                if (response.isSuccessful) {
                    areas.value = response.body()
                } else {
                    _errorMessage.value = "Error al obtener áreas"
                }
            }

            override fun onFailure(call: Call<List<Area>>, t: Throwable) {
                _errorMessage.value = "Error de red: ${t.message}"
            }
        })
    }
    fun filterAreasByZone(zoneId: Int) {
        filteredAreas.value = areas.value?.filter { it.zone_id == zoneId }
    }

}