package com.example.lastbite

import android.app.Application
import android.telecom.Call
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lastbite.models.Area
import com.example.lastbite.models.User
import com.example.lastbite.models.Zone
import retrofit2.Response

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

    fun setUserData(name: String?, email: String?, password: String?) {
        this.name = name
        this.user_email = email
        this.description = name
        this.password = password
    }

    fun setUserLocation(zoneId: Int?, areaId: Int?) {
        this.zone_id = zoneId
        this.area_id = areaId
    }

    fun setUserMobileNumber(mobileNumber: String?) {
        this.mobile_number = mobileNumber
    }

    fun setUserVerificationCode(verificationCode: Int?) {
        this.verification_code = verificationCode
    }


}