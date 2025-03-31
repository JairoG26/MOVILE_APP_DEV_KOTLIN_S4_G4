package com.example.lastbite.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.lastbite.models.Product
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import com.example.lastbite.repositories.ProductRepository

class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products
    private val _product = MutableLiveData<Product?>() // 🔹 Para un solo producto
    val product: LiveData<Product?> get() = _product

    fun loadProductsByStore(storeId: Int) {
        repository.fetchProducts(storeId) { productList ->
            _products.postValue(productList ?: emptyList()) // 🔹 Si es null, mandamos lista vacía
        }
    }

    fun loadProductById(productId: Int) {
        repository.fetchProductById(productId) { product ->
            _product.postValue(product) // 🔹 Guardamos directamente el producto
        }
    }
}