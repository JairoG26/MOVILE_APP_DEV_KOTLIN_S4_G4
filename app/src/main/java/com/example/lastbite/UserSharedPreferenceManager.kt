package com.example.lastbite

import android.content.Context
import android.util.Log
import androidx.core.content.edit

class UserSharedPreferenceManager {

    fun storeUserID(user_id: Int?, appContext: Context) {

        if (user_id != null) {
            val sharedPref = appContext.getSharedPreferences(
                "User_ID", Context.MODE_PRIVATE
            )

            sharedPref.edit {
                putInt("user_id", user_id)
            }
        } else {
            Log.d("UserSPManager.storeUserID", "The User ID is null. Hence, it was not stored.")
        }
    }
}