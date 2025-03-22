package com.example.lastbite

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lastbite.databinding.ActivityCartListBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            buttonCheckout.setOnClickListener {
                showDialog()
            }
        }
    }

    fun showDialog() {

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.activity_checkout)
        val btnCheckout = dialog.findViewById<Button>(R.id.confirmCheckout)

        btnCheckout?.setOnClickListener {
            Toast.makeText(this, "Clicked on Confirm Checkout", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }


}