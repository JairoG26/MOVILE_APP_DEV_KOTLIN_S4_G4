package com.example.lastbite.models

data class SignUpData (

    val name : String,
    val user_email : String,
    val password : String,
    val mobile_number : String?,
    val area_id : Int?,
    val user_type : String,
    val description : String?,
    val verification_code : Int?
)