package com.example.lastbite.repositories

import com.example.lastbite.ApiClient
import com.example.lastbite.ApiService
import com.example.lastbite.models.Product
import com.example.lastbite.models.ProductReceived
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository {
    private val apiService = ApiClient.instance.create(ApiService::class.java)

    fun fetchProducts(storeId: Int, callback: (List<Product>?) -> Unit) {
        apiService.getProductsByStore(storeId).enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    callback(response.body()) // 🔹 Pasamos la lista de productos
                } else {
                    callback(null) // 🔹 En caso de error, devolvemos null
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                callback(null) // 🔹 Error de conexión, también devolvemos null
            }
        })
    }

    fun createProduct(product: Product, callback: (Product?) -> Unit) {
        apiService.createProduct(product).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    callback(response.body()) // Producto generado exitosamente
                } else {
                    callback(null) // Falló la generación
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                callback(null) // Error de red
            }
        })
    }

    fun updateProduct(productId: Int, updatedProduct: Product, callback: (Product?) -> Unit) {
        apiService.updateProduct(productId, updatedProduct).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    callback(response.body()) // Producto creado exitosamente
                } else {
                    callback(null) // Falló la actualización
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                callback(null) // Error de red
            }
        })
    }

    fun fetchProductById(productId: Int, callback: (Product?) -> Unit) {
        apiService.getProductById(productId).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    callback(response.body()) // 🔹 Pasamos el producto recibido
                } else {
                    callback(null) // 🔹 En caso de error, devolvemos null
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                callback(null) // 🔹 Error de conexión, también devolvemos null
            }
        })
    }

    fun deleteProduct(productId: Int, callback: (Boolean) -> Unit) { // () Especifica un tipo de retorno
        apiService.deleteProduct(productId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun deliveryProductReceived(imageString : String, callback: (Boolean) -> Unit) {
        apiService.storeImage(ProductReceived(imageString)).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }

        })
    }

}