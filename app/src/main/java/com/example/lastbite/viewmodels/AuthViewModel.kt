package com.example.lastbite.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authStateRegister = MutableLiveData<Boolean>()
    val authStateRegister: LiveData<Boolean> get() = _authStateRegister
    private val _authStateLogIn = MutableLiveData<Boolean>()
    val authStateLogIn: LiveData<Boolean> get() = _authStateLogIn

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authStateRegister.value = true
                } else {
                    _errorMessage.value = task.exception?.localizedMessage
                }
            }
    }

    fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authStateLogIn.value = true
                } else {
                    _authStateLogIn.value = false
                }
            }
    }
}