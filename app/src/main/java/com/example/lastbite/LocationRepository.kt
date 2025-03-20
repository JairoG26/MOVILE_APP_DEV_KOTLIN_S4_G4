package com.example.lastbite

import retrofit2.Call

class LocationRepository(private val apiService: ApiService) {
    fun getZones(): Call<List<String>> {
        return apiService.getZones()
    }

    fun getAreas(): Call<List<String>> {
        return apiService.getAreas()
    }
}