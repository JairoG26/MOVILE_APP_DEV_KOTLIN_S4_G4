package com.example.lastbite.repositories

import com.example.lastbite.ApiService
import com.example.lastbite.models.Store
import retrofit2.Call

class StoreRepository(private val apiService: ApiService) {
    fun fetchStores(): Call<List<Store>> = apiService.getStores()
}