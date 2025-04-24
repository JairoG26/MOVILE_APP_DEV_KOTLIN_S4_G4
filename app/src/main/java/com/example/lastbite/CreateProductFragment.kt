package com.example.lastbite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lastbite.models.Product
import com.example.lastbite.viewmodels.ProductViewModel

class CreateProductFragment : Fragment() {

    private val productViewModel = ProductViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_create_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etName = view.findViewById<EditText>(R.id.etProductName)
        val etPrice = view.findViewById<EditText>(R.id.etPrice)
        val etDetail = view.findViewById<EditText>(R.id.etDetail)
        val etType = view.findViewById<EditText>(R.id.etType)
        val etScore = view.findViewById<EditText>(R.id.etScore)
        val etImageUrl = view.findViewById<EditText>(R.id.etImageUrl)
        val btnSave = view.findViewById<Button>(R.id.btnSaveProduct)

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val price = etPrice.text.toString().toFloatOrNull() ?: 0f
            val detail = etDetail.text.toString()
            val type = etType.text.toString()
            val score = etScore.text.toString().toFloatOrNull() ?: 0f
            val image = etImageUrl.text.toString()
            val storeId = StoreManager.storeId

            val newProduct = Product(
                product_id = null,
                name = name,
                unit_price = price,
                detail = detail,
                product_type = type,
                score = score,
                image = image,
                store_id = storeId!!
            )

            productViewModel.createProduct(newProduct)

            Toast.makeText(requireContext(), "Producto creado", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
        }
    }
}