package com.example.lastbite.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lastbite.ApiClient
import com.example.lastbite.ApiService
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

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun registerUser(email: String, password: String, name: String, mobile_number: String?, verification_code: Int?, area_id: Int?, user_type: String, description: String?) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authStateRegister.value = true
                    saveUserToBackend(email, name, mobile_number, verification_code, area_id, user_type, description)
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

    private fun saveUserToBackend(email: String, name: String, mobile_number: String?, verification_code: Int?, area_id: Int?, user_type: String, description: String?) {
        val apiService = ApiClient.getRetrofit().create(ApiService::class.java)
        val user = User(area_id, description, mobile_number, name, email, user_type, verification_code)
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