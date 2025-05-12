package com.example.lastbite.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lastbite.ApiClient
import com.example.lastbite.ApiService
import com.example.lastbite.SessionManager
import com.example.lastbite.models.SignUpData
import com.example.lastbite.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authStateRegister = MutableLiveData<Boolean>()
    val authStateRegister: LiveData<Boolean> get() = _authStateRegister
    private val _authStateLogIn = MutableLiveData<Boolean>()
    val authStateLogIn: LiveData<Boolean> get() = _authStateLogIn
    private val _authStateBack = MutableLiveData<Boolean>()
    val authStateBack: LiveData<Boolean> get() = _authStateBack
    val _userType = MutableLiveData<String>()
    val userType: LiveData<String> = _userType
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun registerUser(signUpData: SignUpData) {
        auth.createUserWithEmailAndPassword(signUpData.email, signUpData.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authStateRegister.value = true
                    saveUserToBackend(signUpData)
                } else {
                    _errorMessage.value = task.exception?.localizedMessage
                }
            }
    }

    fun signInUser(email: String, password: String) {
        val apiService = ApiClient.getRetrofit().create(ApiService::class.java)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authStateLogIn.value = true
                    apiService.getUserByEmail(email).enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.isSuccessful && response.body() != null) {
                                val user = response.body()!!
                                _user.value = user
                                SessionManager.saveUser(user)
                                Log.d("DEBUG", "Tiendas recibidass: ${_user.value}")
                                _userType.value = user.user_type // "store" o "customer"
                            } else {
                                _userType.value = "UserType.CUSTOMER" // por defecto si no se encontró el usuario
                            }
                        }
                        override fun onFailure(call: Call<User>, t: Throwable) {
                            _userType.value = "UserType.CUSTOMER" // por defecto si falla la API
                        }
                    })
                } else {
                    _authStateLogIn.value = false
                }
            }
    }

    private fun saveUserToBackend(signUpData: SignUpData) {
        val apiService = ApiClient.getRetrofit().create(ApiService::class.java)
        val user = User(null, signUpData.area_id, signUpData.description, signUpData.user_type, signUpData.name, signUpData.email, signUpData.user_type, signUpData.verification_code)
        val userJson = Gson().toJson(user)
        Log.d("Registro", "JSON enviado: $userJson")
        apiService.registerUser(user).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    _authStateBack.value = true
                } else {
                    _authStateBack.value = false
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _authStateBack.value = false
            }
        })
    }
}