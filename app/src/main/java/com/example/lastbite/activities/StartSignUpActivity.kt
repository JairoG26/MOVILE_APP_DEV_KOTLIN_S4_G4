package com.example.lastbite.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lastbite.R
import com.example.lastbite.databinding.ActivityStartSignUpBinding


class StartSignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStartSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        binding = ActivityStartSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setContentView(R.layout.activity_start_sign_up)

        // val etPhoneNumber: EditText = findViewById(R.id.phoneInput)

        binding.phoneInput.setOnClickListener {
            if (!binding.phoneInput.isFocused) { // Solo si aún no tiene foco
                binding.phoneInput.requestFocus()
            } else {
                val intent = Intent(this, PhoneActivity::class.java)
                startActivity(intent)
            }
        }

        binding.phoneInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val intent = Intent(this, PhoneActivity::class.java)
                binding.phoneInput.clearFocus()
                startActivity(intent)
            }
        }
    }
}