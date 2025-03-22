package com.example.lastbite.repositories

import com.example.lastbite.ApiService
import com.example.lastbite.models.Store

class StoreRepository(private val apiService: ApiService) {
    fun fetchStores(): List<Store> = apiService.getStores()
}