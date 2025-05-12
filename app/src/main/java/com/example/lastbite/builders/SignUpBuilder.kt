package com.example.lastbite.builders

import com.example.lastbite.models.SignUpData

class SignUpBuilder {

    private var email : String = ""
    private var password : String = ""
    private var name : String = ""
    private var mobile_number : String? = ""
    private var verification_code : Int? = 0
    private var area_id : Int? = 0
    private var user_type : String = ""
    private var description : String? = ""

    fun email(value : String) = apply {
        this.email = value
    }

    fun password(value : String) = apply {
        this.password = value
    }

    fun name(value : String) = apply {
        this.name = value
    }

    fun mobile_number(value : String?) = apply {
        this.mobile_number = value
    }

    fun verification_code(value : Int?) = apply {
        this.verification_code = value
    }

    fun area_id(value : Int?) = apply {
        this.area_id = value
    }

    fun user_type(value : String) = apply {
        this.user_type = value
    }

    fun description(value : String?) = apply {
        this.description = value
    }

    fun build() : SignUpData {
        return SignUpData(email, password, name, mobile_number, verification_code, area_id, user_type, description)
    }
}