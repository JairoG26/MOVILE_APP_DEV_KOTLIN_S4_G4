package com.example.lastbite

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://9f7c-186-113-84-35.ngrok-free.app/api/"

    val instance: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    fun getRetrofit(): Retrofit {

        return instance
    }
}