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
import com.example.lastbite.activities.OrderAcceptedActivity
import com.example.lastbite.entities.OrderEntity
import com.example.lastbite.models.Cart
import com.example.lastbite.repositories.LocationRepository
import com.example.lastbite.viewmodels.CheckoutBottomSheetViewModel
import com.example.lastbite.viewmodels.SingletonCartViewModel
import com.example.lastbite.viewmodels.SingletonOrderStatusViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.Instant
import java.time.format.DateTimeFormatter
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class CheckoutBottomSheet : BottomSheetDialogFragment() {

    // private var cartGenerated : Cart? = null
    private var total : Float = 0.0F

    private val checkoutBottomSheetViewModel = CheckoutBottomSheetViewModel()
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
            total = cartItems.sumOf { it.unitPrice.toDouble() * it.quantity }.toFloat()
            costText.text = "$ %.2f".format(total)
        }

        val closeButton = view.findViewById<ImageButton>(R.id.closeCheckoutButton)
        closeButton.setOnClickListener {
            dismiss()
        }

        val confirmButton = view.findViewById<Button>(R.id.confirmCheckout)
        confirmButton.setOnClickListener {

            Toast.makeText(requireContext(), "Processing order....", Toast.LENGTH_SHORT).show()

            val userId = SessionManager.getUser()?.user_id
            val newCart = Cart(cart_id = null, user_id = userId, status = "ACTIVE")

            // Lanzamos la corrutina
            /*viewLifecycleOwner.lifecycleScope.launch {
                val createdCart = cartViewModel.createCartSuspend(newCart)

                if (createdCart == null) {
                    Toast.makeText(requireContext(), "Error al generar el carrito", Toast.LENGTH_SHORT).show()
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
            }*/

            cartViewModel.createCart(newCart)

            cartViewModel.cartGenerated.observe(viewLifecycleOwner) {
                // cartGenerated = it
                // Order generation:
                viewLifecycleOwner.lifecycleScope.launch {

                    checkoutBottomSheetViewModel.storeLastOrder(requireContext(), userId, it, total)
                }
            }


            cartViewModel.getActiveCart(userId)

            cartViewModel.clearCart()

            orderStatusViewModel.isOrderAccepted.value = true

            Toast.makeText(requireContext(), "The order was confirmed.", Toast.LENGTH_SHORT).show()
            dismiss()

            val intent = Intent(requireContext(), OrderAcceptedActivity::class.java)
            startActivity(intent)
        }
    }
}