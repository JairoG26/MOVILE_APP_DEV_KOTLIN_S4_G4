package com.example.lastbite.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.lastbite.HomeActivity
import com.example.lastbite.viewmodels.AuthViewModel
import com.example.lastbite.R

class SignInActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        val emailEditText: EditText = findViewById(R.id.emailInput)
        val passwordEditText: EditText = findViewById(R.id.passwordInput)
        val signInButton: Button = findViewById(R.id.btnSignIn)

        authViewModel.authStateLogIn.observe(this) { isAuthenticated ->
            if (isAuthenticated) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                // Navegar a la siguiente pantalla
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Error en el inicio de sesión", Toast.LENGTH_SHORT).show()
            }
        }

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.signInUser(email, password)
            } else {
                Toast.makeText(this, "Ingresa email y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
