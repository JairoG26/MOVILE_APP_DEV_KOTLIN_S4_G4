package com.example.lastbite

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        setContentView(R.layout.activity_sign_in)

        val tvGoToSignUp: TextView = findViewById(R.id.tvGoToSignUp)

        tvGoToSignUp.setOnClickListener {
            val intent = Intent(this, StartSignUpActivity::class.java)
            startActivity(intent)
        }
    }
}