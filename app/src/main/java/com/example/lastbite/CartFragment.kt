package com.example.lastbite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.lastbite.databinding.FragmentCartBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.apply {
            buttonCheckout.setOnClickListener {
                    showDialog()
            }
        }
        return root
    }

    fun showDialog() {

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.activity_checkout)
        val btnCheckout = dialog.findViewById<Button>(R.id.confirmCheckout)
        // val btnCheckoutExit = dialog.findViewById<Button>(R.id.closeCheckoutButton)

        btnCheckout?.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked on Confirm Checkout", Toast.LENGTH_SHORT).show()
        }

        dialog.show()

        /*btnCheckoutExit?.setOnClickListener {
            dialog.dismiss()
        }*/
    }

}