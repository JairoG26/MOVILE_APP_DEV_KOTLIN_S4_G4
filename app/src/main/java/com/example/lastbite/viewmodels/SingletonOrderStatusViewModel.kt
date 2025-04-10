package com.example.lastbite.viewmodels

import androidx.lifecycle.ViewModelProvider

object SingletonOrderStatusViewModel {
    val instance: OrderStatusViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(OrderStatusViewModel::class.java)
    }
}