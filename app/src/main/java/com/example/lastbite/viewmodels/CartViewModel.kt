package com.example.lastbite.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lastbite.models.Cart
import com.example.lastbite.models.CartItem
import com.example.lastbite.models.CartProduct
import com.example.lastbite.repositories.CartRepository

class CartViewModel: ViewModel() {
    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> get() = _cartItems
    var cartsCount: Int = 0
    var activeCart: Cart? = null
    private val repository = CartRepository()

    fun addItem(item: CartItem): Boolean {
        val currentItems = _cartItems.value ?: emptyList()

        // Si el carrito no está vacío, verificar si el nuevo producto es de la misma tienda
        if (currentItems.isNotEmpty()) {
            val currentStoreId = currentItems.first().storeId
            if (item.storeId != currentStoreId) {
                Log.w("CartViewModel", "Producto no es de la misma tienda.")
                return false // No agregar si la tienda es diferente
            }
        }

        _cartItems.value = currentItems + item
        return true
    }

    fun increaseQuantity(item: CartItem) {
        val currentItems = _cartItems.value?.toMutableList() ?: mutableListOf()

        val index = currentItems.indexOfFirst { it.productId == item.productId }

        if (index != -1) {
            val updatedItem = currentItems[index].copy(quantity = currentItems[index].quantity + 1)
            currentItems[index] = updatedItem
            _cartItems.value = currentItems
        }
    }

    fun decreaseItemQuantity(item: CartItem) {
        val currentItems = _cartItems.value?.toMutableList() ?: mutableListOf()

        val index = currentItems.indexOfFirst { it.productId == item.productId }

        if (index != -1) {
            val updatedItem = currentItems[index].copy(quantity = currentItems[index].quantity - 1)

            if (updatedItem.quantity > 0) {
                currentItems[index] = updatedItem
            } else {
                currentItems.removeAt(index) // Si la cantidad llega a 0, elimina el producto
            }

            _cartItems.value = currentItems
        }
    }

    fun removeItem(item: CartItem) {
        val currentItems = _cartItems.value?.toMutableList() ?: mutableListOf()

        currentItems.removeAll { it.productId == item.productId }

        _cartItems.value = currentItems
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun createCart(cart: Cart) {
        repository.createCart(cart) { createdCart ->
            if (createdCart != null) {
                // Cart creado con éxito
                Log.d("POST", "Cart creado")
            } else {
                // Error al crear Cart
                Log.e("POST", "Error al crear el Cart")
            }
        }
    }
    suspend fun createCartSuspend(cart: Cart): Cart? {
        val createdCart = repository.createCartSuspend(cart)
        if (createdCart != null) {
            Log.d("POST", "Cart creado suspend")
        } else {
            Log.e("POST", "Error al crear el Cart suspend")
        }
        return createdCart
    }

    fun createCartProduct(cartProduct: CartProduct) {
        repository.createCartProduct(cartProduct) { createdCartProduct ->
            if (createdCartProduct != null) {
                // CartProduct creado con éxito
                Log.d("POST", "CartProduct creado: ${createdCartProduct.product_id}")
            } else {
                // Error al crear CartProduct
                Log.e("POST", "Error al crear el CartProduct")
            }
        }
    }

    fun getCarts() {
        repository.getCarts { carts ->
            if (carts != null) {
                // Carts obtenidos con éxito
                Log.d("GET", "Carts obtenidos: ${carts.size}")
                cartsCount = carts.size + 1000

            } else {
                // Error al obtener Carts
                Log.e("GET", "Error al obtener los Carts")
            }
        }
    }

    fun getActiveCart(userId: Int?) {
        if (userId != null) {
            repository.getActiveCart(userId) { cart ->
                if (cart != null) {
                    // Cart activo obtenido con éxito
                    activeCart = cart
                    Log.d("GET", "Cart activo obtenido: ${cart.cart_id}")
                } else {
                    // Error al obtener el Cart activo
                    Log.e("GET", "Error al obtener el Cart activo")
                }
            }
        }
    }

    fun updateCart(cartId: Int?, cart: Cart) {
        if (cartId != null) {
            repository.updateCart(cartId, cart) { updatedCart ->
                if (updatedCart != null) {
                    // Cart actualizado con éxito
                    Log.d("PUT", "Cart actualizado: ${updatedCart.cart_id}")
                    activeCart = null
                } else {
                    // Error al actualizar el Cart
                    Log.e("PUT", "Error al actualizar el Cart")
                }
            }
        }
    }
}