package com.example.lastbite.builders

import com.example.lastbite.models.SignUpData

class SignUpBuilder {

    private var name : String = ""
    private var user_email : String = ""
    private var mobile_number : String? = ""
    private var area_id : Int? = 0
    private var user_type : String = ""
    private var description : String? = ""
    private var verification_code : Int? = 0
    private var password : String = ""

    fun name(value : String) = apply {
        this.name = value
    }

    fun user_email(value : String) = apply {
        this.user_email = value
    }

    fun mobile_number(value : String?) = apply {
        this.mobile_number = value
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

    fun verification_code(value : Int?) = apply {
        this.verification_code = value
    }

    fun password(value : String) = apply {
        this.password = value
    }

    fun build() : SignUpData {
        return SignUpData(name, user_email, password, mobile_number, area_id, user_type, description, verification_code)
    }
}