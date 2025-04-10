package com.example.lastbite.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lastbite.R

class OrderAcceptedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_accepted)

        val backToHomeButton = findViewById<Button>(R.id.btnBackToHome)
        backToHomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)

            // Limpia la pila de actividades para evitar volver con el botón atrás
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(intent)
            finish() // Cierra esta actividad
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_nav_container, fragment).commit()
    }
}