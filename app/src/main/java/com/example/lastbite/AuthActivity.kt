package com.example.lastbite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        setContentView(R.layout.activity_auth)

        val signInButton: Button = findViewById(R.id.btnSignIn)
        val signUpButton: TextView = findViewById(R.id.btnSignUp)


        signInButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, StartSignUpActivity::class.java)
            startActivity(intent)
        }

    }
}