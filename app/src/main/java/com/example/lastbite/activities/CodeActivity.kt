package com.example.lastbite.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lastbite.R
import com.example.lastbite.viewmodels.SingletonSignUpViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CodeActivity : AppCompatActivity() {
    private val signUpViewModel = SingletonSignUpViewModel.instance

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)


        val etCodeInput: EditText = findViewById(R.id.etCodeInput)
        val fabNext: FloatingActionButton = findViewById(R.id.fabNext)

        etCodeInput.requestFocus()
        etCodeInput.postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etCodeInput, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        fabNext.setOnClickListener {
            val code: String = etCodeInput.getText().toString()

            if (code.length == 4 && code.all { it.isDigit() }) {
                val codeInt = code.toInt()
                signUpViewModel.verification_code = codeInt

                val intent = Intent(this, LocationActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Ingresa un código de 4 dígitos", Toast.LENGTH_SHORT).show()
            }

        }
    }

}