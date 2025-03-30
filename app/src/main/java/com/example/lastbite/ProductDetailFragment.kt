package com.example.lastbite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lastbite.viewmodels.ProductViewModel
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import android.widget.ImageView
import android.widget.TextView

class ProductDetailFragment : Fragment() {

    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_food_item_detail, container, false)

        val productId = arguments?.getInt("productId") ?: 0
        productViewModel.loadProductById(productId)

        val productName = view.findViewById<TextView>(R.id.foodItemTitle)
        val productPrice = view.findViewById<TextView>(R.id.costText3)
        val productImage = view.findViewById<ImageView>(R.id.imageView)
        val productDescription = view.findViewById<TextView>(R.id.detailedProductText)

        productViewModel.product.observe(viewLifecycleOwner) { product ->
            product?.let {
                productName.text = it.name
                productPrice.text = "$${it.unit_price}"
                productDescription.text = it.detail
                Glide.with(this).load(it.image).into(productImage)
            }
        }

        return view
    }
}