package com.example.lastbite

import com.example.lastbite.models.Area
import com.example.lastbite.models.Cart
import com.example.lastbite.models.CartItem
import com.example.lastbite.models.CartProduct
import com.example.lastbite.models.Product
import com.example.lastbite.models.Store
import com.example.lastbite.models.User
import com.example.lastbite.models.UserStore
import com.example.lastbite.models.Zone
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("zones")
    fun getZones(): Call<List<Zone>>

    @GET("areas")
    fun getAreas(): Call<List<Area>>

    @POST("users/")
    fun registerUser(@Body user: User): Call<Void>

    @GET("users/email")
    fun getUserByEmail(@Query("email") email: String): Call<User>

    ///// Product Services

    @GET("products")
    fun getProducts(): Call<List<Product>>

    @GET("products/{id}")
    fun getProductById(@Path("id") productId: Int): Call<Product>

    @GET("products/store/{store_id}")
    fun getProductsByStore(@Path("store_id") store_id: Int): Call<List<Product>>

    @POST("products/")
    fun createProduct(@Body product: Product): Call<Product>

    @DELETE("products/{id}")
    fun deleteProduct(@Path("id") productId: Int): Call<Void>

    @PUT("products/{id}")
    fun updateProduct(@Path("id") productId: Int, @Body product: Product): Call<Product>

    @GET("products/store/{store_id}/top3")
    fun getTop3Products(@Path("store_id") store_id: Int): Call<List<Product>>

    /// Stores Services

    @GET("stores/{id}")
    fun getStoreById(@Path("id") storeId: Int): Call<Store>

    @GET("user_store/user/{userId}")
    fun getUserStores(@Path("userId") userId: Int?): Call<List<UserStore>>

    @GET("stores")
    fun getStores(): Call<List<Store>>

    @GET("stores/nearby")
    fun getNearByStores(@Query("lat") lat: Double, @Query("lon") lon: Double): Call<List<Store>>

    /// Carts

    @GET("carts")
    fun getCarts(): Call<List<Cart>>

    @POST("carts/")
    fun createCart(@Body request: Cart): Call<Cart>

    @GET("carts/{id}")
    fun getCartById(@Path("id") cartId: Int): Call<Cart>

    @GET("carts/user/{userId}/active")
    fun getActiveCartByUserId(@Path("userId") userId: Int): Call<Cart>

    @PUT("carts/{id}/status")
    fun updateCart(@Path("id") cartId: Int, @Body request: Cart): Call<Cart>

    /// Carts Products

    @POST("cart_products/")
    fun createCartProduct(@Body request: CartProduct): Call<CartProduct>
}