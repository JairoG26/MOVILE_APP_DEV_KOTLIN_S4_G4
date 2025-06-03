package com.example.lastbite.repositories

import com.example.lastbite.LocalDatabase
import com.example.lastbite.entities.OrderEntity

class OrderRepository {

    fun storeLastOrder(order: OrderEntity) {

        LocalDatabase.db.orderDao().insertOne(order)
    }

    fun getLastIDInserted() : Int {
        return LocalDatabase.db.orderDao().getLastIDInserted()
    }

    fun getLastOrderByID(orderID: Int) : OrderEntity {
        return LocalDatabase.db.orderDao().getOrderByID(orderID)
    }

    fun getLastOrderByUserID(userID: Int) : OrderEntity {
        return LocalDatabase.db.orderDao().getOrderByUserID(userID)
    }

    fun updateLastOrder(orderID: Int, cartID: Int?, generationDate: String?, status: String,
                        billedDate: String?, total: Float, enabled: Boolean, location: String) {

        LocalDatabase.db.orderDao().update(orderID, cartID, generationDate, status,
            billedDate, total, enabled, location)
    }
}