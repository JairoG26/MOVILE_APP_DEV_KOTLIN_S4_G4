package com.example.lastbite.models

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lastbite.R
import com.example.lastbite.viewmodels.SingletonCartViewModel

class CartAdapter (private var items: List<CartItem>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val cartViewModel = SingletonCartViewModel.instance

    fun updateItems(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView = itemView.findViewById(R.id.imgCartItem)
        val tvProductName: TextView = itemView.findViewById(R.id.tvCartProductName)
        val tvProductPrice: TextView = itemView.findViewById(R.id.tvCartProductPrice)
        val tvProductQuantity: TextView = itemView.findViewById(R.id.tvCartProductQuantity)
        val btnDecrease: ImageButton = itemView.findViewById(R.id.btnDecrease)
        val btnIncrease: ImageButton = itemView.findViewById(R.id.btnIncrease)
        val btnRemove: ImageButton = itemView.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context).load(item.image).into(holder.imgProduct)
        holder.tvProductName.text = item.name
        Log.d("DEBUG", "Mostrando producto: ${item.name}")
        holder.tvProductPrice.text = "$${item.unitPrice * item.quantity}"
        holder.tvProductQuantity.text = item.quantity.toString()

        holder.btnIncrease.setOnClickListener {
            cartViewModel.increaseQuantity(item) // Aumenta 1 unidad
        }

        holder.btnDecrease.setOnClickListener {
            cartViewModel.decreaseItemQuantity(item) // Disminuye 1 unidad
        }

        holder.btnRemove.setOnClickListener {
            cartViewModel.removeItem(item) // Elimina del carrito
        }

    }

    override fun getItemCount(): Int = items.size
}