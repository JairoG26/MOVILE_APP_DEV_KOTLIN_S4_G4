package com.example.lastbite.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lastbite.R
import com.example.lastbite.databinding.ActivityCodeBinding
import com.example.lastbite.viewmodels.SingletonSignUpViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCodeBinding
    private val signUpViewModel = SingletonSignUpViewModel.instance

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_code)
        binding = ActivityCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)


        // val etCodeInput: EditText = findViewById(R.id.etCodeInput)
        // val fabNext: FloatingActionButton = findViewById(R.id.fabNext)
        // val etCodeInput: EditText = binding.etCodeInput
        // val fabNext: FloatingActionButton = binding.fabNext

        binding.etCodeInput.requestFocus()
        binding.etCodeInput.postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etCodeInput, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        binding.fabNext.setOnClickListener {
            val code: String = binding.etCodeInput.getText().toString()

            if (code.length == 4 && code.all { it.isDigit() }) {
                val codeInt = code.toInt()
                signUpViewModel.verification_code = codeInt

                val intent = Intent(this, LocationActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter a 4-digit code.", Toast.LENGTH_SHORT).show()
            }

        }
    }

}