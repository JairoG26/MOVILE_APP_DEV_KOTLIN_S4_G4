package com.example.lastbite

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("zones")
    fun getZones(): Call<List<String>>

    @GET("areas")
    fun getAreas(): Call<List<String>>
}