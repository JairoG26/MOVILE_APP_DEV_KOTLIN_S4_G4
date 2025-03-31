package com.example.lastbite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lastbite.databinding.FragmentProductBinding
import com.example.lastbite.models.Product
import com.example.lastbite.models.ProductAdapter
import com.example.lastbite.viewmodels.ProductViewModel

class ProductFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val storeId = arguments?.getInt("storeId") ?: 0 // Recibimos el ID como Int
        productViewModel.loadProductsByStore(storeId)
        return inflater.inflate(R.layout.fragment_product, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerProducts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        productViewModel.products.observe(viewLifecycleOwner) { products ->
            recyclerView.adapter = ProductAdapter(products) { product ->
                goToProductDetail(product)
            }
        }
    }

    private fun goToProductDetail(product: Product) {
        val productDetailFragment = ProductDetailFragment()
        val bundle = Bundle().apply {
            putInt("productId", product.product_id) // Enviamos el ID del producto
        }
        productDetailFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_nav_container, productDetailFragment)
            .addToBackStack(null)
            .commit()
    }
}