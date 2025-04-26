package com.example.lastbite.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.lastbite.SessionManager
import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        super.onCreate(savedInstanceState)
        SessionManager.init(applicationContext)
        val user = SessionManager.getUser()

        if (user != null) {
            if (user.user_type == "UserType.CUSTOMER") {
                startActivity(Intent(this, HomeActivity::class.java))
            } else if (user.user_type == "UserType.STORE") {
                startActivity(Intent(this, StoreHomeActivity::class.java))
            }
        } else {
            startActivity(Intent(this, AuthActivity::class.java))
        }
        finish()
    }
}