package com.example.lastbite

import com.example.lastbite.models.Area
import com.example.lastbite.models.Product
import com.example.lastbite.models.Store
import com.example.lastbite.models.User
import com.example.lastbite.models.Zone
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("zones")
    fun getZones(): Call<List<Zone>>

    @GET("areas")
    fun getAreas(): Call<List<Area>>

    @POST("users/")
    fun registerUser(@Body user: User): Call<Void>

    ///// Product Services

    @GET("products")
    fun getProducts(): Call<List<Product>>

    @GET("products/{id}")
    fun getProductById(@Path("id") productId: Int): Product

    @GET("products/store/{store_id}")
    fun getProductsByStore(@Path("store_id") store_id: Int): List<Product>



    /// Stores Services

    @GET("stores")
    fun getStores(): List<Store>
}