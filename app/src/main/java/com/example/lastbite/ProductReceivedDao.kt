package com.example.lastbite

import androidx.room.Dao
import androidx.room.Query
import com.example.lastbite.models.ProductReceived

@Dao
interface ProductReceivedDao {

    /*@Query("SELECT * FROM  product_received")
    fun getProductsReceivedAll(): List<ProductReceived>*/
}
