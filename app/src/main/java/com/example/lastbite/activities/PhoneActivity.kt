package com.example.lastbite.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lastbite.R
import com.example.lastbite.viewmodels.SingletonSignUpViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PhoneActivity : AppCompatActivity() {

    private val signUpViewModel = SingletonSignUpViewModel.instance

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)


        val etNewPhoneInput: EditText = findViewById(R.id.etPhoneInput)
        val fabNext: FloatingActionButton = findViewById(R.id.fabNext)

        etNewPhoneInput.requestFocus()
        etNewPhoneInput.postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etNewPhoneInput, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        fabNext.setOnClickListener {
            signUpViewModel.mobile_number = etNewPhoneInput.getText().toString()
            Log.d("PHONE NUMBER", "Teléfono: ${signUpViewModel.mobile_number}")
            val intent = Intent(this, CodeActivity::class.java)
            startActivity(intent)
        }
    }
}