package com.example.lastbite.models

data class SignUpData (
    val email : String,
    val password : String,
    val name : String,
    val mobile_number : String?,
    val verification_code : Int?,
    val area_id : Int?,
    val user_type : String,
    val description : String?
)