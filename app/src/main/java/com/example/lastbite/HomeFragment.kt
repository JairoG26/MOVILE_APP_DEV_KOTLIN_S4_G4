package com.example.lastbite

import android.app.Activity
import androidx.fragment.app.viewModels
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lastbite.models.Store
import com.example.lastbite.models.StoreAdapter
import com.example.lastbite.viewmodels.StoreViewModel

class HomeFragment : Fragment() {

    private lateinit var forYouRecyclerView: RecyclerView
    private lateinit var nearbyRecyclerView: RecyclerView
    private lateinit var allStoresRecyclerView: RecyclerView
    private val storeViewModel: StoreViewModel by viewModels()
    private lateinit var storeAdapter: StoreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        val btnCamera = view.findViewById<Button>(R.id.orderConfirmation)
        forYouRecyclerView = view.findViewById(R.id.recyclerForYou)
        nearbyRecyclerView = view.findViewById(R.id.recyclerNearby)
        allStoresRecyclerView = view.findViewById(R.id.recyclerAllStores)
        allStoresRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        nearbyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        forYouRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        storeAdapter = StoreAdapter(emptyList())
        allStoresRecyclerView.adapter = storeAdapter
        nearbyRecyclerView.adapter = storeAdapter
        forYouRecyclerView.adapter = storeAdapter

        storeViewModel.stores.observe(viewLifecycleOwner) { stores ->
            storeAdapter = StoreAdapter(stores) // 🔹 Actualizamos el adaptador con los datos nuevos
            allStoresRecyclerView.adapter = storeAdapter
            nearbyRecyclerView.adapter = storeAdapter
            forYouRecyclerView.adapter = storeAdapter
        }

        storeViewModel.loadStores()

        btnCamera.setOnClickListener {
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        return view
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, storeList: List<Store>) {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = StoreAdapter(storeList)
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(requireContext(), "Image taken", Toast.LENGTH_SHORT).show()
        }
    }
}