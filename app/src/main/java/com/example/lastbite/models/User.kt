package com.example.lastbite.models

data class User(

    val user_id: Int? = -1,
    val name: String = "NA",
    val user_email: String = "NA",
    val mobile_number: String? = "NA",
    val area_id: Int? = -1,
    val user_type: String = "NA",
    val description: String? = "NA",
    val verification_code: Int? = -1
)