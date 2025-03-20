package com.example.lastbite

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "http:// 192.168.80.18:5000"

    val instance: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    fun getRetrofit(): Retrofit {

        return instance
    }
}