package com.example.lastbite.viewmodels

import android.content.Context
import android.util.Log
import com.example.lastbite.entities.OrderEntity
import com.example.lastbite.models.Cart
import com.example.lastbite.repositories.LocationRepository
import com.example.lastbite.repositories.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.format.DateTimeFormatter

class CheckoutBottomSheetViewModel {

    private val locationRepository = LocationRepository()
    private val orderRepository = OrderRepository()

    suspend fun storeLastOrder(context: Context, userId: Int?, cartGenerated: Cart, total : Float) {

        withContext(Dispatchers.IO) {
            val location = locationRepository.readLocation(context)
            Log.d("CheckoutBSVM", location)

            userId?.let {
                val order = orderRepository.getLastOrderByUserID(it)
                if (order != null) {
                    orderRepository.updateLastOrder(order.order_id, cartGenerated.cart_id,
                            DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
                            "ACTIVE", null, total, true, location
                    )
                }
            }

            orderRepository.storeLastOrder(
                OrderEntity(orderRepository.getLastIDInserted() + 1, userId, cartGenerated.cart_id,
                    DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
                    "ACTIVE", null, total, true, location)
            )

        }

    }
}