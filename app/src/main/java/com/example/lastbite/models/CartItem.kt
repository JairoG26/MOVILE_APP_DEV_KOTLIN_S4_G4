package com.example.lastbite.models

data class CartItem(
    val productId: Int,
    val name: String,
    val unitPrice: Float,
    val image: String,
    var quantity: Int,
)