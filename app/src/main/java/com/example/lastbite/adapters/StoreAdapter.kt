package com.example.lastbite.adapters

import android.util.Log
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.lastbite.R
import com.example.lastbite.SessionManager
import com.example.lastbite.models.Store
import com.example.lastbite.models.StoreCount
import com.example.lastbite.repositories.StoreRepository
import com.example.lastbite.viewmodels.HomeViewModel
import com.google.gson.Gson

class StoreAdapter(private val stores: List<Store>, val onItemClick: (Store, Int?) -> Unit) : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    private var user_id : Int? = null
    private val _stateStoreCounted = MutableLiveData<Boolean>()
    private val storeRepository = StoreRepository()

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
            onItemClick(store, user_id) // Llamamos a la función y pasamos el restaurante seleccionado
            /*if (homeViewModel != null) {
                if (user_id != null) {
                    homeViewModel.countStore(store, user_id!!)
                    Log.d("STORE", "object sent: $store")
                }
                else {
                    Log.d("STORE", "user_id is null: $store")
                }
            } else {
                Log.d("STORE", "HomeVM is null: $store")
            }*/
            val currentUser = SessionManager.getUser()
            currentUser?.let {
                val storeCount = StoreCount(null, store.store_id, it.user_id, 0)
                Log.d("StoreAdapter", "The User_ID is: $it.user_id")
                val storeCountJson = Gson().toJson(storeCount)
                storeRepository.countStore(storeCount, callback = {
                    _stateStoreCounted.value = it
                })
                Log.d("StoreAdapter", "StoreCount JSON sent: $storeCountJson")
            }
        }
    }

    override fun getItemCount(): Int = stores.size

    fun updateUserId(userId: Int?) {
        user_id = userId
    }
}