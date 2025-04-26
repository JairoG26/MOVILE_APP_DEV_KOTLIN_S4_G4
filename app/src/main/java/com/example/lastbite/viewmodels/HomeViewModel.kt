package com.example.lastbite.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import android.location.Location
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.lastbite.models.Location as LocationData
import com.example.lastbite.ApiClient
import com.example.lastbite.ApiService
import com.example.lastbite.viewmodels.AuthViewModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    // private val authViewModel: AuthViewModel
    private val _stateBack = MutableLiveData<Boolean>()

    fun sendUserLocation(locationReceived: Location?) {
        (if (locationReceived == null) {
            Log.d("HomeViewModel", "Location is null")
        } else {
            val apiService = ApiClient.getRetrofit().create(ApiService::class.java)
            val location = LocationData(locationReceived.latitude, locationReceived.longitude)
            val locationJson = Gson().toJson(location)
            Log.d("HomeViewModel", "JSON sent: $locationJson")
            apiService.receiveLocation(location).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        _stateBack.value = true
                    } else {
                        _stateBack.value = false
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    _stateBack.value = false
                }
            })
        })
    }

}