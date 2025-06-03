package com.example.lastbite

import com.google.android.gms.location.LocationServices
import android.Manifest
import android.content.Context
import com.example.lastbite.fragments.HomeFragment
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Location
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import com.example.lastbite.repositories.LocationRepository
import com.google.gson.Gson
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class LocationManager {  // It has to be fixed.

    private lateinit var contextGeneral: Context
    private lateinit var viewLCOwner : LifecycleOwner

    var userLocation: Location? = null
    private val locationRepository = LocationRepository()
    private val _stateSendLocation = MutableLiveData<Boolean>()
    private val networkManager = NetworkManager()

    fun requestLocationPermission(context: Context, viewLifecycleOwner: LifecycleOwner) {
        contextGeneral = context
        viewLCOwner = viewLifecycleOwner
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
           // homeFragment.locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getUserLocation(context, viewLifecycleOwner)
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
            Log.d("LocationManager.sendUserLocation", "The location is null.")
        } else {
            if (networkManager.isOnline(context)) {
                val location = com.example.lastbite.models.Location(
                    null,
                    locationReceived.latitude,
                    locationReceived.longitude,
                    0
                )
                val locationJson = Gson().toJson(location)
                locationRepository.sendLocation(location, callback = {
                    _stateSendLocation.value = it
                })
                Log.d("LocationManager.sendUserLocation", "Location JSON sent: $locationJson")
            } else {
                Log.d("LocationManager.sendUserLocation", "The app is in an offline context. The location" +
                        " will be written in a local file.")
                storeLocation(locationReceived, context)
            }
        }
    }

    fun storeLocation(location: Location, context: Context) {

        locationRepository.storeLocation(location, context)
    }

    fun getUserLocation(context: Context, viewLifecycleOwner: LifecycleOwner) {

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val location = fusedLocationClient.lastLocation.await()
                location?.let {
                    userLocation = it
                    Log.d("LocationManager.getUserLocation", "The Location has the following" +
                            " coordinates: Lat: ${it.latitude}, Long: ${it.longitude}")
                    sendUserLocation(userLocation, context)
                }
            } catch (e: Exception) {
                Log.e("LocationManager.getUserLocation", "There was an error" +
                        " retrieving the location", e)
            }
        }
    }
}