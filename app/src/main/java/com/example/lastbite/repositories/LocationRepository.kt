package com.example.lastbite.repositories

import com.example.lastbite.ApiService
import com.example.lastbite.models.Area
import com.example.lastbite.models.Zone
import retrofit2.Call

class LocationRepository(private val apiService: ApiService) {
    fun getZones(): Call<List<Zone>> {
        return apiService.getZones()
    }

    fun getAreas(): Call<List<Area>> {
        return apiService.getAreas()
    }
}