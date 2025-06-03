package com.example.lastbite.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lastbite.entities.OrderEntity
import com.example.lastbite.models.Cart
import com.example.lastbite.models.CartItem
import com.example.lastbite.models.CartProduct
import com.example.lastbite.repositories.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.format.DateTimeFormatter

class CartViewModel: ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> get() = _cartItems
    private val _cartGenerated = MutableLiveData<Cart>()
    val cartGenerated : LiveData<Cart> = _cartGenerated
    var cartsCount: Int = 0
    private val _activeCart = MutableLiveData<Cart?>()
    val activeCart: LiveData<Cart?> = _activeCart
    private val repository = CartRepository()

    fun addItem(item: CartItem): Boolean {

        val currentItems = _cartItems.value ?: emptyList()

        // Si el carrito no está vacío, verificar si el nuevo producto es de la misma tienda
        if (currentItems.isNotEmpty()) {
            val currentStoreId = currentItems.first().storeId
            if (item.storeId != currentStoreId) {
                Log.w("CartVM", "The product is not from the same store.")
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

    fun createCart(cart: Cart) {

        repository.createCart(cart) { createdCart ->
            _cartGenerated.value = createdCart
        }

        Log.d("CartVM.\"generateCart\"", "The function execution just ended.")
    }

    suspend fun createCartSuspend(cart: Cart): Cart {

        val createdCart = repository.createCartSuspend(cart)
        if (createdCart != null) {
            _cartGenerated.value = createdCart
            Log.d("CartVM.generateCartSuspend", "The cart was generated.")
        } else {
            Log.e("CartVM.generateCartSuspend", "There was an error generating the cart.")
        }
        return createdCart
    }

    private fun createCartProduct(cartProduct: CartProduct) {

        Log.d("CartVM.\"createCartProduct\"", "The function was called.")
        repository.createCartProduct(cartProduct) { createdCartProduct ->
            if (createdCartProduct != null) {
                Log.d("CartVM.generateCartProduct", "The cart product with ID " +
                        " ${createdCartProduct.product_id} was generated.")
            } else {
                Log.d("CartVM.generateCartProduct", "There was an error generating the CartProduct " +
                        "entity.")
            }
        }
    }

    private fun generateCartProducts(cartID: Int) {

        Log.d("CartVM.generateCartProducts", "The function has been called.")
        cartItems.value?.forEach { item ->
            Log.d("CartVM.generateCartProducts", "cartItems.value is not null.")
            val newCartProduct = CartProduct(
                cart_id = cartID,
                product_id = item.productId,
                quantity = item.quantity
            )
            createCartProduct(newCartProduct)
        }
        Log.d("CartVM.generateCartProducts", "The function execution ended.")
    }

    fun clearCart() {

        _cartItems.value = emptyList()
    }

    fun getCarts() {

        repository.getCarts { carts ->
            if (carts != null) {
                // Carts obtenidos con éxito
                Log.d("CartVM.getCarts()", "The carts obtained are ${carts.size}.")
                cartsCount = carts.size + 1000

            } else {
                // Error al obtener Carts
                Log.e("CartVM.getCarts()", "There was an error retrieving the carts.")
            }
        }
    }

    fun getActiveCart(userId: Int?) {

        if (userId != null) {
            repository.getActiveCart(userId) { cart ->
                if (cart != null) {
                    // Cart activo obtenido con éxito
                    _activeCart.value = cart
                    Log.d("CartVM.getActiveCart", "The active cart has the following ID: ${cart.cart_id}.")
                    cart.cart_id?.let {
                        generateCartProducts(it)
                        Log.d("CartVM.getActiveCart", "The generateCartProducts function was called.")
                    }
                } else {
                    // Error al obtener el Cart activo
                    Log.e("CartVM.getActiveCart", "There was an error retrieving the active cart.")
                }
            }
        }
    }

    fun updateCart(cartId: Int?, cart: Cart) {

        if (cartId != null) {
            repository.updateCart(cartId, cart) { updatedCart ->
                if (updatedCart != null) {
                    // Cart actualizado con éxito
                    Log.d("CartVM.updateCart", "The updated cart has the following ID:" +
                            " ${updatedCart.cart_id}.")
                    _activeCart.value = null
                } else {
                    // Error al actualizar el Cart
                    Log.e("CartVM.updateCart", "There was an error updating the cart.")
                }
            }
        }
    }
}