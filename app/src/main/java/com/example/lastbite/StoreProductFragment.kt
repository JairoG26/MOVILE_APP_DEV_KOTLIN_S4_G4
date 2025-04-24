package com.example.lastbite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lastbite.models.Product
import com.example.lastbite.models.ProductAdapter
import com.example.lastbite.models.StoreProductAdapter
import com.example.lastbite.viewmodels.ProductViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StoreProductFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val productViewModel: ProductViewModel by viewModels()

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
}