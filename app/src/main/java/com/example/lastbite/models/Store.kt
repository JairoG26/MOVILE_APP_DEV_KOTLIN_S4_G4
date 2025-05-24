package com.example.lastbite.models

data class Store(
    val store_id: Int?,
    val nit: String,
    val name: String,
    val address: String,
    val longitude: Float,
    val latitude: Float,
    val logo: String,
    val opens_at: String,
    val closes_at: String
)
