package com.example.lastbite

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat.getSystemService
import com.example.lastbite.entities.OrderEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.format.DateTimeFormatter

class NetworkManager {

    /*suspend fun init() {
        withContext(Dispatchers.IO) {
            LocalDatabase.db.orderDao().insertOne(
                OrderEntity(
                LocalDatabase.db.orderDao().getLastIDInserted() + 1,
                user_id = 14,
                cart_id = 1,
                creation_date = DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
                status = "ACTIVE",
                billed_date = null,
                total_price = 100.0F,
                enabled = true,
                location = "4.6874157, -74.090998"
            )
            )
        }
    }*/

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
}