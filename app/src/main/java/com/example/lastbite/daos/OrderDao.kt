package com.example.lastbite.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lastbite.entities.OrderEntity

@Dao
interface OrderDao {

    @Insert
    fun insertOne(orderEntity: OrderEntity)

    @Insert
    fun insertAll(vararg orderEntities: OrderEntity)

    @Query("SELECT order_id FROM OrderEntity ORDER BY order_id DESC LIMIT 1")
    fun getLastIDInserted(): Int

    @Query("SELECT * FROM OrderEntity")
    fun getAll(): List<OrderEntity>

    @Query("SELECT * FROM OrderEntity WHERE order_id=(:orderID)")
    fun getOrderByID(orderID: Int): OrderEntity

    @Query("SELECT * FROM OrderEntity WHERE user_id=(:userID)")
    fun getOrderByUserID(userID: Int): OrderEntity

    @Query("DELETE FROM OrderEntity WHERE order_id=(:orderID)")
    fun delete(orderID: Int)

    @Query("UPDATE OrderEntity SET cart_id=(:cartID), creation_date=(:generationDate)," +
            "status=(:status), billed_date=(:billedDate), total_price=(:total), " +
            "enabled=(:enabled), location=(:location)  WHERE order_id=(:orderID)")
    fun update(orderID: Int, cartID: Int?, generationDate: String?, status: String,
               billedDate: String?, total: Float, enabled: Boolean, location: String)
}