package com.example.lastbite.models

data class User(
    //val user_id: Int,
    val area_id: Int?,
    val description: String?,
    val mobile_number: String?,
    val name: String,
    val user_email: String,
    val user_type: String,
    val verification_code: Int?
)