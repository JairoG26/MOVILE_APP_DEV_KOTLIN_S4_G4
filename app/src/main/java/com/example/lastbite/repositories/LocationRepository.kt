package com.example.lastbite.repositories

import android.content.Context
import android.util.Log
import com.example.lastbite.ApiClient
import com.example.lastbite.ApiService
import com.example.lastbite.models.Area
import com.example.lastbite.models.Location
import com.example.lastbite.models.Zone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class LocationRepository() {

    private val apiService = ApiClient.instance.create(ApiService::class.java)
    private lateinit var fileWithLocation: File
    private val fileLocationName: String = "location_stored"

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

    private fun generateFileForStoringLocation(context: Context) {

        fileWithLocation = File(context.filesDir, fileLocationName)
        // fileWithLocation = File(context.cacheDir, fileLocationName)
        Log.d("LOCATION", "File generated.")
    }

    fun storeLocation(location: android.location.Location, context: Context) {

        if (!::fileWithLocation.isInitialized) {
            generateFileForStoringLocation(context)
        }

        val fileContent = "${location.latitude}, ${location.longitude} \n"
        context.openFileOutput(fileLocationName, Context.MODE_APPEND).use {
            it.write(fileContent.toByteArray())
        }

        Log.d("LOCATION", "File written.")
    }
}