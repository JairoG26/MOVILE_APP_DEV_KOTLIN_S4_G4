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
}