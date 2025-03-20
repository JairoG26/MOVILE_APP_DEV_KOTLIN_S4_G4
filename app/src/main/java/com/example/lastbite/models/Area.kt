package com.example.lastbite.models

data class Area(
    val area_id: Int,
    val area_name: String,
    val zone_id: Int,
    val users: List<User>
)
