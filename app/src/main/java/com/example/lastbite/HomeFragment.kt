package com.example.lastbite

import android.app.Activity
import androidx.fragment.app.viewModels
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lastbite.models.Store
import com.example.lastbite.models.StoreAdapter
import com.example.lastbite.viewmodels.ProductViewModel
import com.example.lastbite.viewmodels.SingletonOrderStatusViewModel
import com.example.lastbite.viewmodels.StoreViewModel

class HomeFragment : Fragment() {

    private lateinit var forYouRecyclerView: RecyclerView
    private lateinit var nearbyRecyclerView: RecyclerView
    private lateinit var allStoresRecyclerView: RecyclerView
    private val storeViewModel: StoreViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var storeAdapter: StoreAdapter
    private val orderStatusViewModel = SingletonOrderStatusViewModel.instance
    private var photoBitmap: Bitmap? = null

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


        storeAdapter = StoreAdapter(emptyList()) { store -> goToProductFragment(store) }
        allStoresRecyclerView.adapter = storeAdapter
        nearbyRecyclerView.adapter = storeAdapter
        forYouRecyclerView.adapter = storeAdapter

        storeViewModel.stores.observe(viewLifecycleOwner) { stores ->
            storeAdapter = StoreAdapter(stores) { store -> goToProductFragment(store) }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cameraButton = view.findViewById<LinearLayout>(R.id.CameraLayout)

        orderStatusViewModel.isOrderAccepted.observe(viewLifecycleOwner) { accepted ->
            orderStatusViewModel.isPhotoTaken.observe(viewLifecycleOwner) { photoTaken ->
                cameraButton.visibility = if (accepted && !photoTaken) View.VISIBLE else View.GONE
            }
        }

        cameraButton.setOnClickListener {
            if (photoBitmap != null) {
                orderStatusViewModel.isOrderAccepted.value = false
                photoBitmap = null
            }
        }
    }

    private fun goToProductFragment(store: Store) {
        productViewModel.loadProductsByStore(store.store_id) // Cargar productos en ViewModel

        val productFragment = ProductFragment()
        val bundle = Bundle().apply {
            putInt("storeId", store.store_id) // Guardamos el ID como Int
        }
        productFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_nav_container, productFragment) // Usa el ID del contenedor en tu Activity
            .addToBackStack(null) // Para que el usuario pueda volver atrás
            .commit()
    }


    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                photoBitmap = imageBitmap // ✅ Aquí la almacenas
                orderStatusViewModel.isOrderAccepted.value = false // Ocultas el botón
            }
        }
    }
}