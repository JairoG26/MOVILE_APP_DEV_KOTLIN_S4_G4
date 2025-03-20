package com.example.lastbite

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PhoneActivity : AppCompatActivity() {

    private lateinit var signUpViewModel: SignUpViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        val etNewPhoneInput: EditText = findViewById(R.id.etPhoneInput)
        val fabNext: FloatingActionButton = findViewById(R.id.fabNext)

        etNewPhoneInput.requestFocus()
        etNewPhoneInput.postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etNewPhoneInput, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        fabNext.setOnClickListener {
            signUpViewModel.mobile_number = etNewPhoneInput.text.toString()
            val intent = Intent(this, CodeActivity::class.java)
            startActivity(intent)
        }
    }
}