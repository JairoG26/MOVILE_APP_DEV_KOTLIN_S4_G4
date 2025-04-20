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
    private val _productDeleted = MutableLiveData<Boolean>()
    val productDeleted: LiveData<Boolean> = _productDeleted

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

    fun createProduct(product: Product) {
        repository.createProduct(product) { createdProduct ->
            if (createdProduct != null) {
                // Producto creado con éxito
                Log.d("POST", "Producto creado: ${createdProduct.name}")
            } else {
                // Error al crear producto
                Log.e("POST", "Error al crear el producto")
            }
        }
    }

    fun deleteProduct(productId: Int) {
        repository.deleteProduct(productId) { success ->
            if (success) {
                // Puedes emitir un LiveData para avisar al fragment que se eliminó
                _productDeleted.postValue(true)
            } else {
                _productDeleted.postValue(false)
            }
        }
    }
}