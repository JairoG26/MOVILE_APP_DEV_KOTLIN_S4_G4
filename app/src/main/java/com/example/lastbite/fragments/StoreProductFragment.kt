package com.example.lastbite.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lastbite.R
import com.example.lastbite.models.Product
import com.example.lastbite.adapters.ProductScoreAdapter
import com.example.lastbite.adapters.StoreProductAdapter
import com.example.lastbite.viewmodels.ProductViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StoreProductFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val productViewModel: ProductViewModel by viewModels()
    private var shouldShowDialog = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val storeId = arguments?.getInt("storeId") ?: 0 // Recibimos el ID como Int
        productViewModel.loadProductsByStore(storeId)
        return inflater.inflate(R.layout.fragment_store_product, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var fabAddProduct = view.findViewById<FloatingActionButton>(R.id.fabAddProduct)
        recyclerView = view.findViewById(R.id.recyclerProducts)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        fabAddProduct.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_store_nav_container, CreateProductFragment()) // Usa el ID del contenedor en tu Activity
                .addToBackStack(null) // Para que el usuario pueda volver atrás
                .commit()
        }

        productViewModel.products.observe(viewLifecycleOwner) { products ->
            recyclerView.adapter = StoreProductAdapter(products) { product ->
                goToProductDetail(product)
            }
        }

        productViewModel.top3Products.observe(viewLifecycleOwner) { products ->
            if (shouldShowDialog) {
                showTopProductsDialog(products)
                shouldShowDialog = false
            }
        }

        val fabTopProducts = view.findViewById<ExtendedFloatingActionButton>(R.id.fabTopProducts)

        val storeId = arguments?.getInt("storeId") ?: 0
        fabTopProducts.setOnClickListener {
            shouldShowDialog = true
            productViewModel.getTop3Products(storeId)
        }
    }



    private fun goToProductDetail(product: Product) {
        val productDetailFragment = StoreProductDetailFragment()
        val bundle = Bundle().apply {
            putInt("productId", product.product_id!!) // Enviamos el ID del producto
        }
        productDetailFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_store_nav_container, productDetailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showTopProductsDialog(products: List<Product>) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_product_list, null)
        val listView = dialogView.findViewById<ListView>(R.id.product_list_view)

        val adapter = ProductScoreAdapter(requireContext(), products)
        listView.adapter = adapter

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            //.setPositiveButton("Close", null)
            .create()

        // Hacer el fondo del diálogo transparente
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.principal_green)
        )

        dialog.show()
    }
}