package com.example.lastbite.models

data class User(
    val user_id: Int,
    val area_id: Int,
    val description: String,
    val mobileNumber: String,
    val name: String,
    val email: String,
    val userType: String,
    val verificationCode: Int
)
