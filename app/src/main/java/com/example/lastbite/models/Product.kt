package com.example.lastbite.models

data class Product(
    val product_id: Int,
    val store_id: Int,
    val name: String,
    val product_type: String,
    val unit_price: Float,
    val detail: String,
    val score: Float,
    val image: String
)
