package com.example.lastbite.viewmodels

object SingletonCartViewModel {
    val instance: CartViewModel by lazy {
        CartViewModel()
    }
}