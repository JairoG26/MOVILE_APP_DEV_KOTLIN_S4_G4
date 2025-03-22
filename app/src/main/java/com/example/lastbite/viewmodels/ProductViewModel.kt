package com.example.lastbite.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.lastbite.models.Product
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import com.example.lastbite.repositories.ProductRepository

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _productDetail = MutableLiveData<Product>()
    val productDetail: LiveData<Product> get() = _productDetail

    fun getProductsByStore(storeId: Int) {
        viewModelScope.launch {
            try {
                _products.value = productRepository.fetchProductsByStore(storeId)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error loading products", e)
            }
        }
    }

    fun getProductDetail(productId: Int) {
        viewModelScope.launch {
            try {
                _productDetail.value = productRepository.fetchProductDetail(productId)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error loading product detail", e)
            }
        }
    }
}