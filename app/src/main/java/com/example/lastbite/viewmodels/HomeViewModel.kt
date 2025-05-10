package com.example.lastbite.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import com.example.lastbite.models.Location as LocationData
import com.example.lastbite.ApiClient
import com.example.lastbite.ApiService
import java.io.ByteArrayOutputStream
import android.util.Base64
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import com.example.lastbite.repositories.ProductRepository
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class HomeViewModel : ViewModel() {

    // private val authViewModel: AuthViewModel
    private val _stateBack = MutableLiveData<Boolean>()
    private val _stateUpdatePhoto = MutableLiveData<Boolean>()
    val stateUpdatePhoto : LiveData<Boolean> = _stateUpdatePhoto
    private val repository = ProductRepository()

    fun isOnline(context : Context) : Boolean {
        /* val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()*/

        val connectivityManager = getSystemService(context, ConnectivityManager::class.java) as ConnectivityManager
        // connectivityManager.requestNetwork(networkRequest, networkCallback)

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            else -> false
        }
    }

    fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val earthRadius = 6371.0 // Radio de la Tierra en km

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2.0)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    fun sendUserLocation(locationReceived: Location?) {
        (if (locationReceived == null) {
            Log.d("HomeViewModel", "Location is null")
        } else {
            val apiService = ApiClient.getRetrofit().create(ApiService::class.java)
            val location = LocationData(null, locationReceived.latitude, locationReceived.longitude, 0)
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

    fun storePhoto(image : Bitmap) {
        val image64 = bitmapToBase64(image)
        repository.deliveryProductReceived(image64, callback = {
            _stateUpdatePhoto.value = it
        })
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

}