package com.example.lastbite.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lastbite.R

class StoreProductAdapter(
    private val productList: List<Product>,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<StoreProductAdapter.StoreProductViewHolder>() {

    inner class StoreProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.imgProduct)
        val productName: TextView = view.findViewById(R.id.tvProductName)

        fun bind(product: Product) {
            with(itemView) {
                // Cargar imagen con Glide
                Glide.with(context)
                    .load(product.image)
                    .placeholder(R.drawable.colombiaflagicon)
                    .into(productImage)

                // Asignar texto
                productName.text = product.name

                itemView.setOnClickListener {
                    onProductClick(product) // Llamamos a la función al hacer clic
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_store, parent, false)
        return StoreProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size
}