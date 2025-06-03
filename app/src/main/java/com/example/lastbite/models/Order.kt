package com.example.lastbite.models

data class Order(

    val order_id: Int? = null,
    val user_id: Int, // SessionManager has the user as null
    val cart_id: Int?,
    val creation_date: String? = null,
    val status: String,
    val billed_date: String?,
    val total_price: Float,
    val enabled: Boolean
)
