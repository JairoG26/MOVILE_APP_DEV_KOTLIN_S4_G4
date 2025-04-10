package com.example.lastbite.models

data class Store(
    val store_id: Int,
    val nit: String,
    val name: String,
    val address: String,
    val longitude: Double,
    val latitude: Double,
    val logo: String
)
