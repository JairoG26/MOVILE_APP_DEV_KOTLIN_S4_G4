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
import java.io.ByteArrayOutputStream
import android.util.Base64
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import com.example.lastbite.models.Store
import com.example.lastbite.models.StoreCount
import com.example.lastbite.repositories.LocationRepository
import com.example.lastbite.repositories.ProductRepository
import com.example.lastbite.repositories.StoreRepository
import com.google.gson.Gson
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
    private val repositoryProduct = ProductRepository()
    private val _stateSendLocation = MutableLiveData<Boolean>()
    // val stateSendLocation : LiveData<Boolean> = _stateSendLocation
    private val locationRepository = LocationRepository()
    private val _stateStoreCounted = MutableLiveData<Boolean>()
    private val storeRepository = StoreRepository()

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

    fun calculateDistance (
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

    fun sendUserLocation(locationReceived: Location?, context: Context) {

        if (locationReceived == null) {
            Log.d("HomeVM.sendUserLocation", "The location is null.")
        } else {
            if (isOnline(context)) {
                val location = LocationData(null, locationReceived.latitude, locationReceived.longitude, 0)
                val locationJson = Gson().toJson(location)
                locationRepository.sendLocation(location, callback = {
                    _stateSendLocation.value = it
                })
                Log.d("HomeVM.sendUserLocation", "Location JSON sent: $locationJson")
            } else {
                Log.d("HomeVM.sendUserLocation", "The app is in an offline context. The location" +
                        " will be written in a local file.")
                storeLocation(locationReceived, context)
            }
        }
    }

    fun storeLocation(location: Location, context: Context) {

        locationRepository.storeLocation(location, context)
    }

    fun storePhoto(image : Bitmap) {

        val image64 = bitmapToBase64(image)
        Log.d("HomeVM.storePhoto", "The image was converted to Base64.")
        repositoryProduct.deliveryProductReceived(image64, callback = {
            _stateUpdatePhoto.value = it
        })
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun countStore(store : Store, user_id : Int) {

        if (store == null) {
            Log.d("HomeViewModel", "StoreCount is null")
        }
        val storeCount = StoreCount(null, store.store_id, user_id, 0)
        val storeCountJson = Gson().toJson(storeCount)
        storeRepository.countStore(storeCount, callback = {
            _stateStoreCounted.value = it
        })
        Log.d("HomeViewModel", "StoreCount JSON sent: $storeCountJson")
    }

}