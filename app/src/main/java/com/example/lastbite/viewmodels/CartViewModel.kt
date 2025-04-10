package com.example.lastbite.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lastbite.models.CartItem

class CartViewModel: ViewModel() {
    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    fun addItem(item: CartItem) {
        val currentItems = _cartItems.value ?: emptyList()
        _cartItems.value = currentItems + item
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
}