package com.example.lastbite.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lastbite.R
import com.example.lastbite.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        //setContentView(R.layout.activity_auth)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // val signInButton: Button = findViewById(R.id.btnSignIn)
        // val signUpButton: TextView = findViewById(R.id.btnSignUp)
        // val signInButton: Button = binding.btnSignIn
        // val signUpButton: TextView = binding.btnSignUp

        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, StartSignUpActivity::class.java)
            startActivity(intent)
        }

    }
}