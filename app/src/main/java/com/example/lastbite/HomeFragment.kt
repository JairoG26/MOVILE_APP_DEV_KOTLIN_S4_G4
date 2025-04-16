package com.example.lastbite

import android.app.Activity
import androidx.fragment.app.viewModels
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import android.Manifest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lastbite.models.Store
import com.example.lastbite.models.StoreAdapter
import com.example.lastbite.viewmodels.ProductViewModel
import com.example.lastbite.viewmodels.SingletonOrderStatusViewModel
import com.example.lastbite.viewmodels.StoreViewModel
import com.google.android.gms.location.LocationServices
import android.Manifest
import kotlin.math.*

class HomeFragment : Fragment() {

    private lateinit var forYouRecyclerView: RecyclerView
    private lateinit var nearbyRecyclerView: RecyclerView
    private lateinit var allStoresRecyclerView: RecyclerView
    private val storeViewModel: StoreViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var storeAdapter: StoreAdapter
    private val orderStatusViewModel = SingletonOrderStatusViewModel.instance
    private var photoBitmap: Bitmap? = null
    private var userLocation: Location? = null

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

            if (userLocation != null) {
                val nearbyStores = stores.filter { store ->
                    val distance = calculateDistance(
                        userLocation!!.latitude,
                        userLocation!!.longitude,
                        store.latitude,
                        store.longitude
                    )
                    distance < 1.0
                }

                val nearbyAdapter = StoreAdapter(nearbyStores) { store -> goToProductFragment(store) }
                nearbyRecyclerView.adapter = nearbyAdapter
            } else {
                // Si no hay ubicación aún, muestra todas por ahora
                nearbyRecyclerView.adapter = storeAdapter
            }
            
        }
        storeViewModel.loadStores()

        btnCamera.setOnClickListener {
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
        requestLocationPermission()
        return view
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getUserLocation()
        } else {
            Toast.makeText(requireContext(), "Permiso de ubicación denegado", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getUserLocation()
        }
    }

    private fun getUserLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                userLocation = it
                Log.d("UBICACIÓN", "Latitud: ${it.latitude}, Longitud: ${it.longitude}")
                // Aquí podrías llamar a tu función para filtrar tiendas cercanas
            }
        }
    }

    fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val earthRadius = 6371.0 // Radio de la Tierra en km

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2.0)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
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


    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(requireContext(), "Image taken", Toast.LENGTH_SHORT).show()
            val data = result.data
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                photoBitmap = imageBitmap
                orderStatusViewModel.isOrderAccepted.value = false // Ocultas el botón
            }
        }
    }
}