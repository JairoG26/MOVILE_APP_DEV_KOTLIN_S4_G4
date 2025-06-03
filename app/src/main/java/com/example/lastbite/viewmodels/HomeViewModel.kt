package com.example.lastbite.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import android.location.Location
import android.media.Image
import androidx.lifecycle.MutableLiveData
import com.example.lastbite.models.Location as LocationData
import java.io.ByteArrayOutputStream
import android.util.Base64
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.lastbite.NetworkManager
import com.example.lastbite.SessionManager
import com.example.lastbite.models.Store
import com.example.lastbite.models.StoreCount
import com.example.lastbite.repositories.BannerRepository
import com.example.lastbite.repositories.LocationRepository
import com.example.lastbite.repositories.OrderRepository
import com.example.lastbite.repositories.ProductRepository
import com.example.lastbite.repositories.StoreRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class HomeViewModel : ViewModel() {

    private val networkManager = NetworkManager()

    private val _stateLoadBanner = MutableLiveData<Boolean>()
    // val stateLoadBanner : LiveData<Boolean> = _stateLoadBanner
    // private val bannerRepository = BannerRepository()

    private val _stateUpdatePhoto = MutableLiveData<Boolean>()
    val stateUpdatePhoto : LiveData<Boolean> = _stateUpdatePhoto
    private val repositoryProduct = ProductRepository()

    private val _stateSendLocation = MutableLiveData<Boolean>()
    // val stateSendLocation : LiveData<Boolean> = _stateSendLocation
    private val locationRepository = LocationRepository()

    private val orderRepository = OrderRepository()
    private val _theyHadOrderedHere = MutableLiveData<Boolean>()
    val theyHadOrderedHere : LiveData<Boolean> = _theyHadOrderedHere

    private val _stateStoreCounted = MutableLiveData<Boolean>()
    private val storeRepository = StoreRepository()

    /*fun loadBanner(context: Context) {
        bannerRepository.load(context, callback = {
            _stateLoadBanner.value = it
        })
    }*/

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
            viewModelScope.launch {
                isWhereTheyHadOrdered(locationReceived)
            }

            if (networkManager.isOnline(context)) {
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

        Log.d("HomeVM.storeLocation", "The function execution just started.")
        locationRepository.storeLocation(location, context)
    }

    private suspend fun isWhereTheyHadOrdered(location: Location) {

        withContext(Dispatchers.IO) {
            val locationString = "${location.latitude}, ${location.longitude}"
            val order = SessionManager.getUser()?.user_id?.let {
                orderRepository.getLastOrderByUserID(
                    it
                )
            }
            if (order != null) {
                _theyHadOrderedHere.postValue(locationString == order.location)
            }

        }
    }

    fun countStore(store : Store, user_id : Int?) {

        if (store == null) {
            Log.d("HomeVM.countStore", "The store is null.")
        }
        val storeCount = StoreCount(null, store.store_id, user_id, 0)
        val storeCountJson = Gson().toJson(storeCount)
        storeRepository.countStoreCache(storeCount)
        storeRepository.countStoreNetwork(storeCount, callback = {
            _stateStoreCounted.value = it
        })
        Log.d("HomeVM.countStore", "StoreCount JSON sent: $storeCountJson")
    }

    suspend fun storePhoto(image : Bitmap) {

        withContext(Dispatchers.IO) {
            Log.d("HomeVM.storePhoto", "The IO coroutine code just started to be executed.")
            repositoryProduct.deliveryProductReceivedCache(image)
            withContext(Dispatchers.IO) {
                val image64 = bitmapToBase64(image)
                Log.d("HomeVM.storePhoto", "The image was converted to Base64.")
                repositoryProduct.deliveryProductReceivedNetwork(image64, callback = {
                    _stateUpdatePhoto.value = it
                })
            }
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}