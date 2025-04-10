package com.example.lastbite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lastbite.models.CartAdapter
import com.example.lastbite.viewmodels.SingletonCartViewModel

class CartFragment : Fragment() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private val cartViewModel = SingletonCartViewModel.instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        cartRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        cartAdapter = CartAdapter(emptyList())
        cartRecyclerView.adapter = cartAdapter

        // Observas el LiveData
        cartViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartAdapter.updateItems(cartItems)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Encuentra el botón y configura el listener
        val checkoutBtn = view.findViewById<View>(R.id.btnCheckout)
        checkoutBtn.setOnClickListener {
            val bottomSheet = CheckoutBottomSheet()
            bottomSheet.show(parentFragmentManager, "CheckoutBottomSheet")
        }
    }
}
