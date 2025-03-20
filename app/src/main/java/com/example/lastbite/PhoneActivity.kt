package com.example.lastbite

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PhoneActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        val etNewPhoneInput: EditText = findViewById(R.id.etPhoneInput)

        etNewPhoneInput.requestFocus()
        etNewPhoneInput.postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etNewPhoneInput, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }


}