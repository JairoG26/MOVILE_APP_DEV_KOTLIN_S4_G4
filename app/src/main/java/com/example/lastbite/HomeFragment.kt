package com.example.lastbite

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.Manifest
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lastbite.models.Cart
import com.example.lastbite.models.Store
import com.example.lastbite.models.StoreAdapter
import com.example.lastbite.viewmodels.HomeViewModel
import com.example.lastbite.viewmodels.ProductViewModel
import com.example.lastbite.viewmodels.SingletonCartViewModel
import com.example.lastbite.viewmodels.SingletonOrderStatusViewModel
import com.example.lastbite.viewmodels.StoreViewModel
import com.google.android.gms.location.LocationServices
import kotlin.math.*

class HomeFragment : Fragment() {

    private lateinit var forYouRecyclerView: RecyclerView
    private lateinit var nearbyRecyclerView: RecyclerView
    private lateinit var allStoresRecyclerView: RecyclerView
    private val storeViewModel: StoreViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var storeAdapter: StoreAdapter
    private val orderStatusViewModel = SingletonOrderStatusViewModel.instance
    private val cartViewModel = SingletonCartViewModel.instance
    private var photoBitmap: Bitmap? = null
    private var userLocation: Location? = null

    /*private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
        }
    }*/

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

        if (!isOnline()) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Lost connection")
            .setMessage("You require an active connection to continue using the app. Please reconnect.")
            .setPositiveButton("Try again"){ dialog, which ->
                if (isOnline()) {
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

    private fun isOnline() : Boolean {
        /* val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()*/

        val connectivityManager = getSystemService(requireContext(), ConnectivityManager::class.java) as ConnectivityManager
        // connectivityManager.requestNetwork(networkRequest, networkCallback)

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            else -> false
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
                orderStatusViewModel.isOrderAccepted.value = false // Ocultas el botón
            }
        }
    }
}