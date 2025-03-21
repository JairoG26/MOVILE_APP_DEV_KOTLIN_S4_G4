package com.example.lastbite

object SingletonSignUpViewModel {
    val instance: SignUpViewModel by lazy {
        SignUpViewModel()
    }
}