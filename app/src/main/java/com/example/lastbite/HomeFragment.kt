package com.example.lastbite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class HomeFragment : Fragment() {

    // private late init var imageView : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)
        val btnCamera = view.findViewById<Button>(R.id.orderConfirmation)
        // imageView = view.findViewById<ImageView>(R.id.orderRestaurant)
        btnCamera.setOnClickListener{
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
        return view
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(requireContext(), "Image taken", Toast.LENGTH_SHORT).show()
            // val intent = result.data
            // val imageBitmap = intent?.extras?.get("data") as Bitmap
            // imageView.setImageBitmap(imageBitmap)
        }
    }

}