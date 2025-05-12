package com.example.lastbite.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lastbite.CheckoutBottomSheet
import com.example.lastbite.R
import com.example.lastbite.adapters.CartAdapter
import com.example.lastbite.viewmodels.HomeViewModel
import com.example.lastbite.viewmodels.SingletonCartViewModel

class CartFragment : Fragment() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private val cartViewModel = SingletonCartViewModel.instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        cartRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        cartAdapter = CartAdapter(emptyList())
        cartRecyclerView.adapter = cartAdapter

        val emptyCartMessage = view.findViewById<TextView>(R.id.emptyCartMessage)
        val checkoutBtn = view.findViewById<View>(R.id.btnCheckout)

        // Observa el LiveData
        cartViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartAdapter.updateItems(cartItems)

            val isEmpty = cartItems.isEmpty()
            emptyCartMessage.visibility = if (isEmpty) View.VISIBLE else View.GONE
            checkoutBtn.visibility = if (isEmpty) View.GONE else View.VISIBLE
        }

        // Configura el botón
        checkoutBtn.setOnClickListener {
            val bottomSheet = CheckoutBottomSheet()
            bottomSheet.show(parentFragmentManager, "CheckoutBottomSheet")
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Encuentra el botón y configura el listener
        val checkoutBtn = view.findViewById<View>(R.id.btnCheckout)
        checkoutBtn.setOnClickListener {
            /*getUserLocation()
            homeViewModel.sendUserLocation(userLocation)*/
            val bottomSheet = CheckoutBottomSheet()
            bottomSheet.show(parentFragmentManager, "CheckoutBottomSheet")
        }
    }
}
