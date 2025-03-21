package com.example.lastbite.viewmodels

object SingletonSignUpViewModel {
    val instance: SignUpViewModel by lazy {
        SignUpViewModel()
    }
}