package com.example.lastbite.repositories

import com.example.lastbite.ApiClient
import com.example.lastbite.ApiService
import com.example.lastbite.models.Cart
import com.example.lastbite.models.CartItem
import com.example.lastbite.models.CartProduct
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartRepository {
    private val apiService = ApiClient.instance.create(ApiService::class.java)

    fun createCart(cart: Cart, callback: (Cart?) -> Unit) {
        apiService.createCart(cart).enqueue(object : Callback<Cart> {
            override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }
            override fun onFailure(call: Call<Cart>, t: Throwable) {
                callback(null)
            }
        })
    }

    suspend fun createCartSuspend(cart: Cart): Cart? = suspendCancellableCoroutine { cont ->
        createCart(cart) { createdCart ->
            cont.resume(createdCart, null)
        }
    }

    fun createCartProduct(cartProduct: CartProduct, callback: (CartProduct?) -> Unit) {
        apiService.createCartProduct(cartProduct).enqueue(object : Callback<CartProduct> {
            override fun onResponse(call: Call<CartProduct>, response: Response<CartProduct>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<CartProduct>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun getCarts(callback: (List<Cart>?) -> Unit) {
        apiService.getCarts().enqueue(object : Callback<List<Cart>> {
            override fun onResponse(call: Call<List<Cart>>, response: Response<List<Cart>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Cart>>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun getActiveCart(userId: Int, callback: (Cart?) -> Unit) {
        apiService.getActiveCartByUserId(userId).enqueue(object : Callback<Cart> {
            override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Cart>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun updateCart(cartId: Int, cart: Cart, callback: (Cart?) -> Unit) {
        apiService.updateCart(cartId, cart).enqueue(object : Callback<Cart> {
            override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Cart>, t: Throwable) {
                callback(null)
            }
        })
    }
}