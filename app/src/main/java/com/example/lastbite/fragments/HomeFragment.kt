package com.example.lastbite.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lastbite.models.Store
import com.example.lastbite.adapters.StoreAdapter
import com.example.lastbite.viewmodels.HomeViewModel
import com.example.lastbite.viewmodels.SingletonOrderStatusViewModel
import com.example.lastbite.viewmodels.StoreViewModel
import com.google.android.gms.location.LocationServices
import androidx.lifecycle.lifecycleScope
import com.example.lastbite.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeFragment : Fragment() {

    private lateinit var forYouRecyclerView: RecyclerView
    private lateinit var nearbyRecyclerView: RecyclerView
    private lateinit var allStoresRecyclerView: RecyclerView
    private val storeViewModel: StoreViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
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

        storeAdapter = StoreAdapter(emptyList(), null) {
                store -> goToProductFragment(store)
        }

        allStoresRecyclerView.adapter = storeAdapter
        nearbyRecyclerView.adapter = storeAdapter
        forYouRecyclerView.adapter = storeAdapter

        homeViewModel.stateUpdatePhoto.observe(viewLifecycleOwner) {
            if (it) {
                orderStatusViewModel.isOrderAccepted.value = false // Ocultas el botón
            } else {
                Toast.makeText(requireContext(), "The photo was not uploaded", Toast.LENGTH_SHORT).show()
            }
        }

        storeViewModel.stores.observe(viewLifecycleOwner) { stores ->
            storeAdapter = StoreAdapter(stores, homeViewModel) { store -> goToProductFragment(store) }
            allStoresRecyclerView.adapter = storeAdapter
            //nearbyRecyclerView.adapter = storeAdapter
            forYouRecyclerView.adapter = storeAdapter
        }

        storeViewModel.nearByStores.observe(viewLifecycleOwner) { stores ->
            val adapter = StoreAdapter(stores, homeViewModel) { store -> goToProductFragment(store) }
            nearbyRecyclerView.adapter = adapter
        }

        requestLocationPermission()
        storeViewModel.loadStores()

        btnCamera.setOnClickListener {
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
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
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getUserLocation()
        }
    }

    /*private fun getUserLocation() {
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
                homeViewModel.sendUserLocation(userLocation)
                Log.d("UBICACIÓN", "Latitud: ${it.latitude}, Longitud: ${it.longitude}")
                // Aquí podrías llamar a tu función para filtrar tiendas cercanas
                storeViewModel.loadNearByStores(it.latitude, it.longitude)
            }
        }
    }*/

    private fun getUserLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val location = fusedLocationClient.lastLocation.await()
                location?.let {
                    userLocation = it
                    homeViewModel.sendUserLocation(it)
                    /*
                    if (homeViewModel.isOnline(requireContext())) {
                        homeViewModel.sendUserLocation(it)
                    }
                    else {
                        homeViewModel.storeLocation(it, requireContext())
                        Log.d("UBICACIÓN", "No hay conexión. Se intentará escribir en un archivo.")
                    }
                     */
                    Log.d("UBICACIÓN", "Lat: ${it.latitude}, Long: ${it.longitude}")
                    storeViewModel.loadNearByStores(it.latitude, it.longitude)
                }
            } catch (e: Exception) {
                Log.e("UBICACIÓN", "Error al obtener ubicación", e)
            }
        }
    }

    private fun goToProductFragment(store: Store) {

        val productFragment = ProductFragment()
        val bundle = Bundle().apply {
            putInt("storeId", store.store_id) // Guardamos el ID como Int
        }
        productFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_nav_container, productFragment) // Usa el ID del contenedor en tu Activity
            .addToBackStack(null) // Para que el usuario vuelva a atrás
            .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val cameraButton = view.findViewById<LinearLayout>(R.id.CameraLayout)

        getUserLocation()

        if (!homeViewModel.isOnline(requireContext())) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Lost connection")
                .setMessage("You require an active connection to continue using the app. Please reconnect.")
                .setPositiveButton("Try again"){ dialog, which ->
                    if (homeViewModel.isOnline(requireContext())) {
                        dialog.dismiss()
                    }
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }

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

            getUserLocation()
            homeViewModel.sendUserLocation(userLocation)
            val data = result.data
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                photoBitmap = imageBitmap
                homeViewModel.storePhoto(imageBitmap)
                // orderStatusViewModel.isOrderAccepted.value = false // Ocultas el botón
                // Glide.with(this).load(imageBitmap).into(view.findViewById(R.id.storeImage))
            } else {
                Log.d("IMAGE", "The image is null")
            }
        }
    }
}