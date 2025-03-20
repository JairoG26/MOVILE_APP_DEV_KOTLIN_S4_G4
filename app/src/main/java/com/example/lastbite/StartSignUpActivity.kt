package com.example.lastbite

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class StartSignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        setContentView(R.layout.activity_start_sign_up)

        val etPhoneNumber: EditText = findViewById(R.id.phoneInput)

        etPhoneNumber.setOnClickListener {
            if (!etPhoneNumber.isFocused) { // Solo si aún no tiene foco
                etPhoneNumber.requestFocus()
            } else {
                val intent = Intent(this, PhoneActivity::class.java)
                startActivity(intent)
            }
        }

        etPhoneNumber.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val intent = Intent(this, PhoneActivity::class.java)
                etPhoneNumber.clearFocus()
                startActivity(intent)
            }
        }
    }
}