package com.example.lastbite

import android.content.Context
import android.content.SharedPreferences
import com.example.lastbite.models.User
import com.google.gson.Gson

object SessionManager {

    private const val PREFS_NAME = "user_session"
    private const val KEY_USER_JSON = "user_data"

    private lateinit var preferences: SharedPreferences
    var currentUser: User? = null
        private set

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        loadUserFromPrefs()
    }

    fun saveUser(user: User) {
        currentUser = user
        val userJson = Gson().toJson(user)
        preferences.edit().putString(KEY_USER_JSON, userJson).apply()
    }

    private fun loadUserFromPrefs() {
        val userJson = preferences.getString(KEY_USER_JSON, null)
        if (userJson != null) {
            currentUser = Gson().fromJson(userJson, User::class.java)
        }
    }

    fun getUser(): User? {
        return currentUser
    }

    fun logout() {
        currentUser = null
        preferences.edit().remove(KEY_USER_JSON).apply()
    }
}