package com.example.lastbite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

class SignInActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        val emailEditText: EditText = findViewById(R.id.emailInput)
        val passwordEditText: EditText = findViewById(R.id.passwordInput)
        val signInButton: Button = findViewById(R.id.btnSignIn)

        authViewModel.authState.observe(this) { isAuthenticated ->
            if (isAuthenticated) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                // Navegar a la siguiente pantalla
                //startActivity(Intent(this, MainActivity::class.java))
                //finish()
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
