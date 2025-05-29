package com.example.lastbite.repositories

import android.graphics.Bitmap
import android.util.Log
import com.example.lastbite.ApiClient
import com.example.lastbite.ApiService
import com.example.lastbite.ProductReceivedLRUCacheManager
import com.example.lastbite.models.Product
import com.example.lastbite.models.ProductReceived
import com.example.lastbite.models.Store
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository {

    private val apiService = ApiClient.instance.create(ApiService::class.java)
    private val prLRUCacheManager = ProductReceivedLRUCacheManager()

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

    fun updateStore(storeId: Int, updatedStore: Store, callback: (Store?) -> Unit) {

        apiService.updateStore(storeId, updatedStore).enqueue(object : Callback<Store> {
            override fun onResponse(call: Call<Store>, response: Response<Store>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null) // Falló la actualización
                }
            }
            override fun onFailure(call: Call<Store>, t: Throwable) {
                callback(null)
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
    
    fun deliveryProductReceivedNetwork(imageString : String, callback: (Boolean) -> Unit) {

        apiService.storeImage(ProductReceived(null, imageString)).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
                // Log.d("PRODUCT_RECEIVED", "Location JSON sent: $locationJson")
                Log.d("ProductRepo", "The image was stored in the backend service.")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
                Log.d("ProductRepo", "The image was not stored in the backend service.")
            }
        })
    }

    fun calculateLeastVisitedStore(userID: Int, callback: (Boolean, Store) -> Unit) {

        apiService.leastVisitedStore(userID).enqueue(object : Callback<Store> {
            override fun onResponse(call: Call<Store>, response: Response<Store>) {
                val leastVisitedStore = response.body()
                if (response.isSuccessful) {
                    if (leastVisitedStore != null) {
                        callback(true, leastVisitedStore)
                        Log.d("ProductRepo", "The least visited store was correctly retrieved.")
                    } else {
                        callback(true, Store(null, "No NIT", "No NAME", "No ADDRESSS",
                            0.0F, 0.0F, "No LOGO", "Closed", "..."))
                        Log.d("ProductRepo", "The least visited store is null.")
                    }
                } else {
                    callback(false, Store(null, "No NIT", "No NAME", "No ADDRESSS",
                        0.0F, 0.0F, "No LOGO", "Closed", "..."))
                    Log.d("ProductRepo", "The least visited store was not retrieved from the backend service.")
                }
            }

            override fun onFailure(call: Call<Store>, t: Throwable) {
                callback(false, Store(null, "No NIT", "No NAME", "No ADDRESSS",
                    0.0F, 0.0F, "No LOGO", "Closed", "..."))
                Log.d("ProductRepo", "The API response for the least visited store failed.")
            }
        })
    }

    fun deliveryProductReceivedCache(image : Bitmap) {

        prLRUCacheManager.init()
        prLRUCacheManager.addBitmapToMemoryCache(image)
    }

    fun getDeliveryProductReceivedFromCache(key : String) : Bitmap {
        return prLRUCacheManager.getBitmapFromMemoryCache(key)
    }

    fun getTop3Products(storeId: Int, callback: (List<Product>?) -> Unit) {

        apiService.getTop3Products(storeId).enqueue(object : Callback<List<Product>> {
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


}