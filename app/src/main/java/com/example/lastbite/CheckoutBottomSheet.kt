package com.example.lastbite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.lastbite.activities.OrderAcceptedActivity
import com.example.lastbite.viewmodels.SingletonCartViewModel
import com.example.lastbite.viewmodels.SingletonOrderStatusViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CheckoutBottomSheet : BottomSheetDialogFragment() {

    private val cartViewModel = SingletonCartViewModel.instance
    private val orderStatusViewModel = SingletonOrderStatusViewModel.instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val costText = view.findViewById<TextView>(R.id.costText)

        cartViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            val total = cartItems.sumOf { it.unitPrice.toDouble() * it.quantity.toInt() }
            costText.text = "$ %.2f".format(total)
        }

        val closeButton = view.findViewById<ImageButton>(R.id.closeCheckoutButton)
        closeButton.setOnClickListener {
            dismiss()
        }

        val confirmButton = view.findViewById<Button>(R.id.confirmCheckout)
        confirmButton.setOnClickListener {
            orderStatusViewModel.isOrderAccepted.value = true
            cartViewModel.clearCart()
            Toast.makeText(requireContext(), "Pedido confirmado", Toast.LENGTH_SHORT).show()

            dismiss() // Cierra el BottomSheet

            // Iniciar nueva actividad
            val intent = Intent(requireContext(), OrderAcceptedActivity::class.java)
            startActivity(intent)

        }
    }
}