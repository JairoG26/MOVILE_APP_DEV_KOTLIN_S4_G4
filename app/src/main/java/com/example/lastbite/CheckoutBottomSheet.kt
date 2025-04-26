package com.example.lastbite

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.lastbite.activities.OrderAcceptedActivity
import com.example.lastbite.models.Cart
import com.example.lastbite.models.CartProduct
import com.example.lastbite.viewmodels.SingletonCartViewModel
import com.example.lastbite.viewmodels.SingletonOrderStatusViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
            val userId = SessionManager.getUser()?.user_id
            val status = "ACTIVE"
            val newCart = Cart(cart_id = null, user_id = userId, status = status)

            // Lanzamos la corrutina
            viewLifecycleOwner.lifecycleScope.launch {
                val createdCart = cartViewModel.createCartSuspend(newCart)

                if (createdCart == null) {
                    Toast.makeText(requireContext(), "Error al crear el carrito", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val activeCart = withContext(Dispatchers.IO) {
                    cartViewModel.getActiveCart(userId)
                    delay(100) // opcional: esperar a que se actualice LiveData
                    cartViewModel.activeCart
                }

                if (activeCart == null) {
                    Toast.makeText(requireContext(), "Error al obtener el carrito activo", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                withContext(Dispatchers.IO) {
                    cartViewModel.cartItems.value?.forEach { item ->
                        val newCartProduct = CartProduct(
                            product_id = item.productId,
                            cart_id = activeCart.cart_id,
                            quantity = item.quantity
                        )
                        cartViewModel.createCartProduct(newCartProduct)
                    }
                }

                cartViewModel.clearCart()
                orderStatusViewModel.isOrderAccepted.value = true

                Toast.makeText(requireContext(), "Pedido confirmado", Toast.LENGTH_SHORT).show()
                dismiss()

                val intent = Intent(requireContext(), OrderAcceptedActivity::class.java)
                startActivity(intent)
            }
        }
    }
}