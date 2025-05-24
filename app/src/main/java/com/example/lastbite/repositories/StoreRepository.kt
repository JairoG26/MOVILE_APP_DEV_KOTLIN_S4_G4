package com.example.lastbite.repositories

import android.util.Log
import com.example.lastbite.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.lastbite.ApiService
import com.example.lastbite.models.Product
import com.example.lastbite.models.Store
import com.example.lastbite.models.StoreCount
import com.example.lastbite.models.StoreList
import com.example.lastbite.models.UserStore
class StoreRepository {

    private val apiService = ApiClient.instance.create(ApiService::class.java)

    // Función que obtiene tiendas por los IDs de usuario
    fun fetchStoresByIds(storeIds: List<Int>, callback: (List<Store>?) -> Unit) {
        val stores = mutableListOf<Store>()
        val calls = storeIds.map { storeId ->
            // Hacemos la llamada para cada storeId
            apiService.getStoreById(storeId)
        }

        // Llamamos a todas las tiendas simultáneamente
        val allCalls = calls.map { call ->
            call.enqueue(object : Callback<Store> {
                override fun onResponse(call: Call<Store>, response: Response<Store>) {
                    if (response.isSuccessful) {
                        response.body()?.let { store ->
                            stores.add(store) // Añadimos la tienda a la lista
                        }
                    }
                    // Continuamos con las demás solicitudes, sin importar si alguna falla
                    if (stores.size == storeIds.size) {
                        callback(stores) // Llamamos el callback cuando tengamos todas las tiendas
                    }
                }

                override fun onFailure(call: Call<Store>, t: Throwable) {
                    // Si una llamada falla, continuamos con las demás, pero no añadimos la tienda
                    if (stores.size == storeIds.size) {
                        callback(stores) // Llamamos el callback cuando tengamos todas las tiendas
                    }
                }
            })
        }
    }

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

    fun fetchNearbyStores(lat: Double, lon: Double, callback: (List<Store>?) -> Unit) {
        apiService.getNearByStores(lat, lon).enqueue(object : Callback<List<Store>> {
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
    
    fun countStore(storeCount: StoreCount, callback: (Boolean) -> Unit) {
        apiService.receiveStoreCount(storeCount).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun getTop1Store(userId: Int?, callback: (Boolean, Store) -> Unit) {
        apiService.getTop1Store(userId).enqueue(object : Callback<Store> {
            override fun onResponse(call: Call<Store>, response: Response<Store>) {
                val top1Stores = response.body()
                if (response.isSuccessful) {
                    if (top1Stores != null) {
                        callback(true, top1Stores)
                    } else {
                        callback(true, Store(null, "No NIT", "No NAME", "No ADDRESSS",
                            0.0F, 0.0F, "No LOGO", "Closed", "..."))
                    }
                } else {
                    callback(false, Store(null, "No NIT", "No NAME", "No ADDRESSS",
                        0.0F, 0.0F, "No LOGO", "Closed", "..."))
                }
            }

            override fun onFailure(call: Call<Store>, t: Throwable) {
                callback(false, Store(null, "No NIT", "No NAME", "No ADDRESSS",
                    0.0F, 0.0F, "No LOGO", "Closed", "..."))
            }
        })
    }

    fun createStore(store: Store, userId: Int, callback: (Store?) -> Unit) {
        apiService.createStore(store).enqueue(object : Callback<Store> {
            override fun onResponse(call: Call<Store>, response: Response<Store>) {
                val createdStore = response.body()
                if (response.isSuccessful && createdStore != null) {

                    // Segunda llamada: crear relación usuario-tienda
                    val relationRequest = UserStore(userId, createdStore.store_id!!)
                    apiService.createUserStore(relationRequest).enqueue(object : Callback<UserStore> {
                        override fun onResponse(call: Call<UserStore>, response: Response<UserStore>) {
                            if (response.isSuccessful) {
                                callback(createdStore)
                            } else {
                                callback(null)
                            }
                        }

                        override fun onFailure(call: Call<UserStore>, t: Throwable) {
                            callback(null)
                        }
                    })
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Store>, t: Throwable) {
                callback(null)
            }
        })
    }
}