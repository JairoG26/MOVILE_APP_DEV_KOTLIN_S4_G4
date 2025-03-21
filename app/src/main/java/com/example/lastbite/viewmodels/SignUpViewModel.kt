package com.example.lastbite.viewmodels

import androidx.lifecycle.ViewModel

class SignUpViewModel() : ViewModel() {

    var user_id: Int? = null
    var name: String? = null
    var user_email: String? = null
    var zone_id: Int? = null
    var area_id: Int? = null
    var description: String? = null
    var mobile_number: String? = null
    val user_type: String = "UserType.CUSTOMER"
    var verification_code: Int? = null
    var password: String? = null

}