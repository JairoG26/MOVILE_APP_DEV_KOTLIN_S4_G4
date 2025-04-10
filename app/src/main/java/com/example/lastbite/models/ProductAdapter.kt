package com.example.lastbite.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lastbite.R

class ProductAdapter(
    private val productList: List<Product>,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.imgProduct)
        val productName: TextView = view.findViewById(R.id.tvProductName)
        val productUnitPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val btnAddToCart: ImageButton = view.findViewById(R.id.addToCartButton)

        fun bind(product: Product) {
            with(itemView) {
                // Cargar imagen con Glide
                Glide.with(context)
                    .load(product.image)
                    .placeholder(R.drawable.colombiaflagicon)
                    .into(productImage)

                // Asignar texto
                productName.text = product.name
                //tvProductDescription.text = "${product.quantity} pcs, ${product.unit}"
                productUnitPrice.text = "$${product.unit_price}"

                // Evento de clic en el botón de agregar
                //btnAddToCart.setOnClickListener { onAddClick(product) }

                itemView.setOnClickListener {
                    onProductClick(product) // Llamamos a la función al hacer clic
                }

                // Cuando hagan click específicamente en el botón "Add to Cart"
                btnAddToCart.setOnClickListener {
                    onProductClick(product)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size
}