package com.example.lastbite.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderStatusViewModel : ViewModel() {
    val isOrderAccepted = MutableLiveData(false)
    val isPhotoTaken = MutableLiveData(false)

    fun reset() {
        isOrderAccepted.value = false
        isPhotoTaken.value = false
    }
}
