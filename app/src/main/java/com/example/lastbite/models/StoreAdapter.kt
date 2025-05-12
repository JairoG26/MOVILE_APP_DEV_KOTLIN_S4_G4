package com.example.lastbite.models

import android.util.Log
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lastbite.R
import com.example.lastbite.viewmodels.HomeViewModel

class StoreAdapter(private val stores: List<Store>, private val homeViewModel: HomeViewModel?, val onItemClick: (Store) -> Unit) : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    private var user_id : Int? = null

    class StoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val storeImage: ImageView = view.findViewById(R.id.storeImage)
        val storeName: TextView = view.findViewById(R.id.storeName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store_logo, parent, false)
        return StoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = stores[position]
        holder.storeName.text = store.name
        Log.d("DEBUG", "Mostrando tienda: ${store.name}")
        Glide.with(holder.itemView.context).load(store.logo).into(holder.storeImage)

        holder.itemView.setOnClickListener {
            onItemClick(store) // Llamamos a la función y pasamos el restaurante seleccionado
            if (homeViewModel != null && user_id != null) {
                homeViewModel.countStore(store, user_id!!)
                Log.d("STORE", "object sent: $store")
            } else {
                Log.d("STORE", "HomeVM is null: $store")
            }
        }
    }

    override fun getItemCount(): Int = stores.size

    fun updateUserId(userId: Int?) {
        user_id = userId
    }
}