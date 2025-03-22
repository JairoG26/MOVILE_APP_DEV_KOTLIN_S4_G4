package com.example.lastbite

import com.example.lastbite.models.Area
import com.example.lastbite.models.User
import com.example.lastbite.models.Zone
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("zones")
    fun getZones(): Call<List<Zone>>

    @GET("areas")
    fun getAreas(): Call<List<Area>>

    @POST("users/")
    fun registerUser(@Body user: User): Call<Void>
}