package com.example.lastbite.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderEntity(

    @PrimaryKey val order_id: Int,
    @ColumnInfo(name = "user_id") val user_id: Int?, // SessionManager might return it with a null value
    @ColumnInfo(name = "cart_id") val cart_id: Int?,
    @ColumnInfo(name = "creation_date") val creation_date: String?,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "billed_date") val billed_date: String?,
    @ColumnInfo(name = "total_price") val total_price: Float,
    @ColumnInfo(name = "enabled") val enabled: Boolean,
    @ColumnInfo(name = "location") val location: String
)