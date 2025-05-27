package com.example.lastbite.repositories

import android.content.Context
import android.util.Log
import com.example.lastbite.ApiClient
import com.example.lastbite.ApiService
import com.example.lastbite.SessionManager
import com.example.lastbite.models.SignUpData
import com.example.lastbite.models.User
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.lastbite.UserSharedPreferenceManager

class UserRepository {

    private val apiService = ApiClient.getRetrofit().create(ApiService::class.java)
    private val userSharedPreferenceManager = UserSharedPreferenceManager()

    fun signInUser(email: String, callback: (Boolean, User) -> Unit) {

        apiService.getUserByEmail(email).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        callback(true, response.body()!!)
                        SessionManager.saveUser(response.body()!!)
                    } else {
                        callback(true, User())
                    }
                } else {
                    callback(false, User())
                    Log.d("UserRepo.signInUser", "The response was not \"successful\", and" +
                            " the user was not found.")
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                callback(false, User())
                Log.d("UserRepo.signInUser", "The API response failed.")
            }
        })
    }

    fun storeUserInBackend(signUpData: SignUpData, callback: (Boolean) -> Unit) {

        val user = User(null, signUpData.name, signUpData.user_email, signUpData.mobile_number,
            signUpData.area_id,  signUpData.user_type, signUpData.description, signUpData.verification_code)
        val userJson = Gson().toJson(user)
        Log.d("AuthVM", "User JSON sent: $userJson")
        apiService.registerUser(user).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                    Log.d("UserRepo.storeUserInBackend", "The user was not stored.")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
                Log.d("UserRepo.storeUserInBackend", "The API response failed.")
            }
        })
    }

    fun storeUserID(user_id: Int?, appContext: Context) {

        userSharedPreferenceManager.storeUserID(user_id, appContext)
    }
}