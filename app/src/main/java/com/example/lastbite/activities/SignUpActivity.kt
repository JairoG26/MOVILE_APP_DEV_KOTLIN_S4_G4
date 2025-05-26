package com.example.lastbite.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.lastbite.viewmodels.AuthViewModel
import com.example.lastbite.R
import com.example.lastbite.builders.SignUpBuilder
import com.example.lastbite.databinding.ActivitySignUpBinding
import com.example.lastbite.viewmodels.SingletonSignUpViewModel
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private val signUpViewModel = SingletonSignUpViewModel.instance
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        // setContentView(R.layout.activity_sign_up)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        auth = FirebaseAuth.getInstance()

        // val etName: EditText = findViewById(R.id.nameInput)
        // val etEmail: EditText = findViewById(R.id.emailInput)
        // val etPassword: EditText = findViewById(R.id.passwordInput)
        // val etConfirmPassword: EditText = findViewById(R.id.confirmPasswordInput)
        // val btnRegister: Button = findViewById(R.id.btnSignUp)
        // val tvGoToSignIn: TextView = findViewById(R.id.tvGoToSignIn)

        binding.tvGoToSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        authViewModel.authStateRegister.observe(this) { isAuthenticated ->
            if (isAuthenticated) {
                Toast.makeText(this, "Sign up completed.", Toast.LENGTH_SHORT).show()
                // Aquí podrías navegar a otra actividad
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "There was an error signing up.", Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSignUp.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()
            val name = binding.nameInput.text.toString().trim()
            val area_id = signUpViewModel.area_id
            val description = signUpViewModel.description
            val mobile_number = signUpViewModel.mobile_number
            val user_type = signUpViewModel.user_type
            val verification_code = signUpViewModel.verification_code

            if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() && password == binding.confirmPasswordInput.text.toString().trim()) {
                // authViewModel.registerUser(email, password, name, mobile_number, verification_code, area_id, user_type, description)
                val signUpData = SignUpBuilder()
                    .user_email(email)
                    .password(password)
                    .name(name)
                    .mobile_number(mobile_number)
                    .verification_code(verification_code)
                    .area_id(area_id)
                    .user_type(user_type)
                    .description(description)
                    .build()
                authViewModel.registerUser(signUpData)
            } else {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}