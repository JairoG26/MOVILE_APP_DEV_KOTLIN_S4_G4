package com.example.lastbite.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.lastbite.viewmodels.ProductViewModel
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.lastbite.R
import com.example.lastbite.SessionManager
import com.example.lastbite.models.CartItem
import com.example.lastbite.viewmodels.SingletonCartViewModel

class ProductDetailFragment : Fragment() {

    private val cartViewModel = SingletonCartViewModel.instance
    private val productViewModel: ProductViewModel by viewModels()

    private val notificationManager = com.example.lastbite.NotificationManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_food_item_detail, container, false)

        val productId = arguments?.getInt("productId") ?: 0
        productViewModel.loadProductById(productId)

        val backButton = view.findViewById<ImageButton>(R.id.backArrowButton2)
        val productName = view.findViewById<TextView>(R.id.foodItemTitle)
        val productPrice = view.findViewById<TextView>(R.id.costText3)
        val productImage = view.findViewById<ImageView>(R.id.imageView)
        val productDescription = view.findViewById<TextView>(R.id.detailedProductText)
        var quantity = 1
        val addButton = view.findViewById<ImageButton>(R.id.addToCartButton2)
        val removeButton = view.findViewById<ImageButton>(R.id.removeFromCartButton2)
        val quantityText = view.findViewById<TextView>(R.id.foodItemQuantityText2)
        val addToBasketButton = view.findViewById<Button>(R.id.buttonCheckout2)


        productViewModel.product.observe(viewLifecycleOwner) { product ->
            product?.let {
                productName.text = it.name
                productPrice.text = "$${it.unit_price}"
                productDescription.text = it.detail
                Glide.with(this).load(it.image).into(productImage)
            }
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        productViewModel.product.observe(viewLifecycleOwner) { product ->
            product?.let {
                productName.text = it.name
                productPrice.text = "$${it.unit_price}"
                productDescription.text = it.detail
                Glide.with(this).load(it.image).into(productImage)

                // Cuando ya cargue el producto, inicializamos precio y cantidad
                quantity = 1
                quantityText.text = quantity.toString()
                productPrice.text = "$${String.format("%.2f", it.unit_price * quantity)}"
            }
        }

        addButton.setOnClickListener {
            quantity++
            quantityText.text = quantity.toString()
            val currentProduct = productViewModel.product.value
            currentProduct?.let {
                productPrice.text = "$${String.format("%.2f", it.unit_price * quantity)}"
            }
        }

        removeButton.setOnClickListener {
            if (quantity > 1) {  // No dejamos que baje de 1
                quantity--
                quantityText.text = quantity.toString()
                val currentProduct = productViewModel.product.value
                currentProduct?.let {
                    productPrice.text = "$${String.format("%.2f", it.unit_price * quantity)}"
                }
            }
        }

        addToBasketButton.setOnClickListener {
            val currentProduct = productViewModel.product.value
            currentProduct?.let { product ->
                val cartItem = CartItem(
                    productId = product.product_id!!,
                    name = product.name,
                    unitPrice = product.unit_price,
                    image = product.image,
                    quantity = quantity,
                    storeId = product.store_id,
                    cart_id = null
                )
                val success = cartViewModel.addItem(cartItem)
                if (success) {
                    Toast.makeText(requireContext(), "${product.name} added to the cart!", Toast.LENGTH_SHORT).show()
                    val currentUser = SessionManager.getUser()
                    if (currentUser != null) {
                        currentUser.user_id?.let { it1 ->
                            Log.d("PDFragment", "The UserID is ${currentUser.user_id}")
                            productViewModel.calculateLeastVisitedStore(
                                it1
                            )
                        }
                    }

                } else {
                    Toast.makeText(requireContext(), "You may only add products of a single store at the same time.", Toast.LENGTH_LONG).show()
                }
            }
        }

        val contextFragment : Context = requireContext()

        notificationManager.generateNotificationChannel(contextFragment, "Least Visited Store")

        productViewModel.leastVisitedStore.observe(viewLifecycleOwner) { store_name ->
            val builder = notificationManager.generateNotificationLeastVisitedStore(store_name, contextFragment,
                "Least Visited Store")
            requestNotificationPermission(contextFragment, builder)
        }

        return view
    }

    private fun requestNotificationPermission(contextFragment: Context, builder: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(contextFragment)) {
            if (ActivityCompat.checkSelfPermission(
                    contextFragment,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            notify(1, builder.build())
        }
    }

    private val notificationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Notification permission granted.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Notification permission denied.", Toast.LENGTH_LONG).show()
        }
    }
}