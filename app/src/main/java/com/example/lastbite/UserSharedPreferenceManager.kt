package com.example.lastbite

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit

class UserSharedPreferenceManager {

    private lateinit var sharedPref : SharedPreferences
    private var initialized : Boolean = false

    private fun init(appContext: Context) {

        sharedPref = appContext.getSharedPreferences(
            "User_ID", Context.MODE_PRIVATE
        )
        initialized = true
    }

    fun storeUserID(user_id: Int?, appContext: Context) {

        if (user_id != null) {

            if (!initialized) {
                init(appContext)
            }

            sharedPref.edit {
                putInt("user_id", user_id)
            }
        } else {
            Log.d("UserSPManager.storeUserID", "The User ID is null. Hence, it was not stored.")
        }
    }

    fun getUserID(): Int {
        return sharedPref.getInt("user_id", 0)
    }
}