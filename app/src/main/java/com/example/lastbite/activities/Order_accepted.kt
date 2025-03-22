package com.example.lastbite.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.lastbite.R

class Order_accepted : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_accepted)

        val btnContinue = findViewById<Button>(R.id.btn_continue)
        btnContinue.setOnClickListener {
            setContentView(R.layout.activity_home) // Cierra esta actividad y vuelve a la anterior
        }
    }
}