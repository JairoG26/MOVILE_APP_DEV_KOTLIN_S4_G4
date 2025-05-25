package com.example.lastbite.activities

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.lastbite.SessionManager
import com.example.lastbite.databinding.ActivitySignInBinding
import com.example.lastbite.viewmodels.AuthViewModel


class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_sign_in)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        /*val emailEditText: EditText = findViewById(R.id.emailInput)
        val passwordEditText: EditText = findViewById(R.id.passwordInput)
        val signInButton: Button = findViewById(R.id.btnSignIn)*/
        // val emailEditText: EditText = binding.emailInput
        // val passwordEditText: EditText = binding.passwordInput
        // val signInButton: Button = binding.btnSignIn

        authViewModel.authStateLogIn.observe(this) { isAuthenticated ->
            if (isAuthenticated) {
                Toast.makeText(this, "Sing in completed.", Toast.LENGTH_SHORT).show()

                authViewModel.user.observe(this) { user ->
                    SessionManager.saveUser(user)
                }

                authViewModel.userType.observe(this) { type ->
                    when (type) {
                        "UserType.STORE" -> {
                            startActivity(Intent(this, StoreHomeActivity::class.java))
                        }
                        "UserType.CUSTOMER" -> {
                        startActivity(Intent(this, HomeActivity::class.java))
                    } else -> {
                        startActivity(Intent(this, HomeActivity::class.java))
                    }
                    }
                    finish()
                }
            } else {
                Toast.makeText(this, "There was an error signing in.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.signInUser(email, password)
            } else {
                Toast.makeText(this, "Please enter the email and password.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGoToSignUp.setOnClickListener {
            val intent = Intent(this, StartSignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
