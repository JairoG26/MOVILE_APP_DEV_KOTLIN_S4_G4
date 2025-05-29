package com.example.lastbite.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.lastbite.models.Product
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import android.util.LruCache
import com.example.lastbite.models.Store
import com.example.lastbite.repositories.ProductRepository

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products
    private val _product = MutableLiveData<Product?>() // Para un solo producto
    val product: LiveData<Product?> get() = _product
    private val _productDeleted = MutableLiveData<Boolean>()
    val productDeleted: LiveData<Boolean> = _productDeleted
    private val _productUpdated = MutableLiveData<Boolean>()
    val productUpdated: LiveData<Boolean> = _productUpdated
    private val _top3Products = MutableLiveData<List<Product>>()
    val top3Products: LiveData<List<Product>> get() = _top3Products
    private val topProductsCache = object : LruCache<Int, List<Product>>(3) {} // Cache para los productos
    private val _stateLeastVisitedStore = MutableLiveData<Boolean>()
    private val _leastVisitedStore = MutableLiveData<String>()
    val leastVisitedStore : LiveData<String> = _leastVisitedStore

    fun loadProductsByStore(storeId: Int) {
        repository.fetchProducts(storeId) { productList ->
            _products.postValue(productList ?: emptyList()) // Si es null, mandamos lista vacía
        }
    }

    fun loadProductById(productId: Int) {
        repository.fetchProductById(productId) { product ->
            _product.postValue(product) // Guardamos directamente el producto
        }
    }

    fun createProduct(product: Product) {
        repository.createProduct(product) { createdProduct ->
            if (createdProduct != null) {
                // Producto generar con éxito
                Log.d("ProductVM", "The following product was generated: ${createdProduct.name}")
            } else {
                // Error al generar producto
                Log.e("ProductVM", "There was an error generating the product.")
            }
        }
    }

    fun updateStore(storeId: Int, updatedStore: Store) {
        repository.updateStore(storeId, updatedStore) { updatedStore ->
            if (updatedStore != null) {
                // Producto generado con éxito
                Log.d("ProductVM", "The following store was updated: ${updatedStore.name}")
            } else {
                // Error al generar producto
                Log.e("ProductVM", "There was an error updating the store.")
            }
        }
    }

    fun deleteProduct(productId: Int) {

        repository.deleteProduct(productId) { success ->
            if (success) {
                _productDeleted.postValue(true)
            } else {
                _productDeleted.postValue(false)
            }
        }
    }

    fun updateProduct(productId: Int, updatedProduct: Product) {

        repository.updateProduct(productId, updatedProduct) { updatedProduct ->
            if (updatedProduct != null) {
                _productUpdated.postValue(true)
            } else {
                _productUpdated.postValue(false)
            }
        }
    }

    fun hayConexion(context: Context): Boolean {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun getTop3Products(storeId: Int) {

        val cached = topProductsCache[storeId]
        if (cached != null) {
            _top3Products.postValue(cached)
        } else {
            repository.getTop3Products(storeId) { productList ->
                _top3Products.postValue(
                    productList ?: emptyList()
                ) // 🔹 Si es null, mandamos lista vacía
                topProductsCache.put(storeId, productList ?: emptyList()) // Actualizamos el cache
            }
        }
    }

    fun calculateLeastVisitedStore(userID: Int) {

        repository.calculateLeastVisitedStore(userID) { callback, store_name ->
            _stateLeastVisitedStore.value = callback
            _leastVisitedStore.value = store_name.name
        }
    }

}