package com.example.lastbite.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lastbite.R
import com.example.lastbite.databinding.ActivityPhoneBinding
import com.example.lastbite.viewmodels.SingletonSignUpViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PhoneActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPhoneBinding
    private val signUpViewModel = SingletonSignUpViewModel.instance

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneBinding.inflate(layoutInflater)
        // setContentView(R.layout.activity_phone)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)


        // val etNewPhoneInput: EditText = findViewById(R.id.etPhoneInput)
        // val fabNext: FloatingActionButton = findViewById(R.id.fabNext)

        binding.etPhoneInput.requestFocus()
        binding.etPhoneInput.postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etPhoneInput, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        binding.fabNext.setOnClickListener {
            signUpViewModel.mobile_number = binding.etPhoneInput.getText().toString()
            Log.d("PHONE NUMBER", "Phone: ${signUpViewModel.mobile_number}")
            val intent = Intent(this, CodeActivity::class.java)
            startActivity(intent)
        }
    }
}