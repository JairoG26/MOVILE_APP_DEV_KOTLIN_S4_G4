package com.example.lastbite.repositories

import com.example.lastbite.ApiService
import com.example.lastbite.models.Product

class ProductRepository(private val apiService: ApiService) {
    fun fetchProductsByStore(store_id: Int): List<Product> = apiService.getProductsByStore(store_id)
    fun fetchProductDetail(productId: Int): Product = apiService.getProductById(productId)
}