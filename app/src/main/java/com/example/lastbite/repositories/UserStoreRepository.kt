package com.example.lastbite.repositories

import com.example.lastbite.ApiClient
import com.example.lastbite.ApiService
import com.example.lastbite.models.Store
import com.example.lastbite.models.UserStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserStoreRepository {

    private val apiService = ApiClient.instance.create(ApiService::class.java)

    fun getUserStoresByUserId(userId: Int?, callback: (List<Int>?, String?) -> Unit) {
        apiService.getUserStores(userId).enqueue(object : Callback<List<UserStore>> {
            override fun onResponse(call: Call<List<UserStore>>, response: Response<List<UserStore>>) {
                if (response.isSuccessful) {
                    val storeIds = response.body()?.map { it.store_id } ?: emptyList()
                    callback(storeIds, null)
                } else {
                    callback(null, "No se pudo obtener las tiendas del usuario")
                }
            }

            override fun onFailure(call: Call<List<UserStore>>, t: Throwable) {
                callback(null, "Error: ${t.message}")
            }
        })
    }
}