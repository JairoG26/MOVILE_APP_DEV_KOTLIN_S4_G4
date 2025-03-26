package com.example.lastbite.repositories

import android.util.Log
import com.example.lastbite.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.lastbite.ApiService
import com.example.lastbite.models.Store

class StoreRepository {
    private val apiService = ApiClient.instance.create(ApiService::class.java)

    fun fetchStores(callback: (List<Store>?) -> Unit) {
        apiService.getStores().enqueue(object : Callback<List<Store>> {
            override fun onResponse(call: Call<List<Store>>, response: Response<List<Store>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Store>>, t: Throwable) {
                callback(null)
            }
        })
    }
}