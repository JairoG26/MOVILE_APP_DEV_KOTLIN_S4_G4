package com.example.lastbite.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.lastbite.R
import com.example.lastbite.StoreManager
import com.example.lastbite.models.Product
import com.example.lastbite.viewmodels.ProductViewModel

class StoreProductDetailFragment : Fragment() {

    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_store_product_detail, container, false)

        val productId = arguments?.getInt("productId") ?: 0
        productViewModel.loadProductById(productId)

        val productName = view.findViewById<EditText>(R.id.etName)
        val productPrice = view.findViewById<EditText>(R.id.etPrice)
        val productImage = view.findViewById<ImageView>(R.id.ivImage)
        val productDetail = view.findViewById<EditText>(R.id.etDetail)
        val productType = view.findViewById<TextView>(R.id.etType)
        val productScore = view.findViewById<EditText>(R.id.etScore)
        val imageUrl = view.findViewById<EditText>(R.id.etImageUrl)
        val deleteButton = view.findViewById<Button>(R.id.btnDelete)
        val saveButton = view.findViewById<Button>(R.id.btnSave)
        val spinner: Spinner = view.findViewById(R.id.spinner_status)
        val options = arrayOf("PRODUCT", "SUBSCRIPTION")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        deleteButton.setOnClickListener {
            if (!productViewModel.hayConexion(requireContext())) {
                Toast.makeText(requireContext(), "You need to have internet to do this", Toast.LENGTH_SHORT).show()
            } else {
                productViewModel.deleteProduct(productId)
            }
        }

        saveButton.setOnClickListener {
            if (!productViewModel.hayConexion(requireContext())) {
                Toast.makeText(requireContext(), "You need to have internet to do this", Toast.LENGTH_SHORT).show()
            } else {
                val updatedProduct = Product(
                    product_id = null,
                    name = productName.text.toString(),
                    unit_price = productPrice.text.toString().toFloat(),
                    detail = productDetail.text.toString(),
                    product_type = spinner.selectedItem.toString(),
                    score = productScore.text.toString().toFloat(),
                    image = imageUrl.text.toString(),
                    store_id = StoreManager.storeId!!
                )
                productViewModel.updateProduct(productId, updatedProduct)
            }
        }

        productViewModel.productDeleted.observe(viewLifecycleOwner) { wasDeleted ->
            if (wasDeleted) {
                Toast.makeText(requireContext(), "Product deleted", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            } else {
                Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
        }
        productViewModel.productUpdated.observe(viewLifecycleOwner) { wasUpdated ->
            if (wasUpdated) {
                Toast.makeText(requireContext(), "Product Updated", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            } else {
                Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }

        productViewModel.product.observe(viewLifecycleOwner) { product ->
            product?.let {
                productName.setText(it.name)
                productPrice.setText(it.unit_price.toString())
                productDetail.setText(it.detail)
                productType.setText(it.product_type)
                productScore.setText(it.score.toString())
                imageUrl.setText(it.image)
                Glide.with(this).load(it.image).into(productImage)
            }
        }
        return view
    }
}