package com.example.lastbite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.lastbite.models.Store
import com.example.lastbite.viewmodels.StoreViewModel

class HomeFragment : Fragment() {
    private lateinit var storeViewModel: StoreViewModel
    private lateinit var layoutStores: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
        layoutStores = requireView().findViewById(R.id.layoutLStores)

        storeViewModel = ViewModelProvider(this).get(StoreViewModel::class.java)
        storeViewModel.getStores()

        storeViewModel.stores.observe(viewLifecycleOwner) { stores ->
            showLogos(stores)
        }
        return view
    }
    private fun showLogos(restaurantes: List<Store>) {
        layoutStores.removeAllViews()

        for (restaurante in restaurantes) {
            val cardView = CardView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 16)
                }
                radius = 20f
                cardElevation = 8f
            }

            val imageView = ImageView(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(200, 200)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }

            Glide.with(this)
                .load(restaurante.logo)
                .into(imageView)

            cardView.addView(imageView)
            layoutStores.addView(cardView)
        }
    }

}