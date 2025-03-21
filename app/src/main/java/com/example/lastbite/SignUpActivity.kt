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
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val signUpViewModel = SingletonSignUpViewModel.instance
    private lateinit var authViewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        val etName: EditText = findViewById(R.id.nameInput)
        val etEmail: EditText = findViewById(R.id.emailInput)
        val etPassword: EditText = findViewById(R.id.passwordInput)
        val etConfirmPassword: EditText = findViewById(R.id.confirmPasswordInput)
        val btnRegister: Button = findViewById(R.id.btnSignUp)
        val tvGoToSignIn: TextView = findViewById(R.id.tvGoToSignIn)

        tvGoToSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        authViewModel.authState.observe(this) { isAuthenticated ->
            if (isAuthenticated) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                // Aquí podrías navegar a otra actividad
            }
        }

        authViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val name = etName.text.toString().trim()

            if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() && password == etConfirmPassword.text.toString().trim()) {
                authViewModel.registerUser(email, password)
            } else {
                Toast.makeText(this, "Por favor ingresa todos los campos", Toast.LENGTH_SHORT).show()
            }

            authViewModel.registerUser(email, password)
        }
    }
}