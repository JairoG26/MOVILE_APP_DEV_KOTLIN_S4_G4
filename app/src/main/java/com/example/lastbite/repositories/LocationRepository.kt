package com.example.lastbite.repositories

import android.content.Context
import android.util.Log
import com.example.lastbite.ApiClient
import com.example.lastbite.ApiService
import com.example.lastbite.LocationFileManager
import com.example.lastbite.models.Area
import com.example.lastbite.models.Location
import com.example.lastbite.models.Zone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationRepository() {

    private val apiService = ApiClient.instance.create(ApiService::class.java)
    private val locationFileManager = LocationFileManager()

    fun getZones(): Call<List<Zone>> {
        return apiService.getZones()
    }

    fun getAreas(): Call<List<Area>> {
        return apiService.getAreas()
    }

    fun sendLocation(location : Location, callback: (Boolean) -> Unit) {

        apiService.receiveLocation(location).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun storeLocation(location: android.location.Location, context: Context) {

        Log.d("LocationRepo.storeLocation", "The function execution just started.")
        locationFileManager.storeLocation(location, context)
    }

    fun readLocation(context: Context) : String {
        return locationFileManager.readLocation(context)
    }
}