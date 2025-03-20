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
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CodeActivity : AppCompatActivity() {
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
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        }
    }
}