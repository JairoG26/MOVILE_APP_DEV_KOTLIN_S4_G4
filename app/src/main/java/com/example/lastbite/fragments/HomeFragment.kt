package com.example.lastbite.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
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
import com.example.lastbite.LocalDatabase
import com.example.lastbite.LocationManager
import com.example.lastbite.NetworkManager
import com.example.lastbite.ProductReceivedLRUCacheManager
import com.example.lastbite.R
import com.example.lastbite.entities.OrderEntity
import com.example.lastbite.models.Order
import com.example.lastbite.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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
    // private val locationManager = LocationManager()
    private val notificationManager = com.example.lastbite.NotificationManager()
    private val networkManager = NetworkManager()
    // private val imageKitManager: ImageKitManager = ImageKitManager()
    // private val productRepository = ProductRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        // val imageMT = view.findViewById<ImageView>(R.id.imageMT)
        // homeViewModel.loadBanner(requireContext())
        /*imageKitManager.initService(requireContext().applicationContext)
        ImageKit.getInstance()
            .url(
                src = "https://ik.imagekit.io/lastbite/banner",
                // transformationPosition = TransformationPosition.PATH
            )
            .setResponsive(
                view = view.findViewById<ImageView>(R.id.imageMT))
            .create()*/

        /*viewLifecycleOwner.lifecycleScope.launch {
            networkManager.init()
        }*/

        val contextFragment : Context = requireContext()

        if (!networkManager.isOnline(contextFragment)) {
            val builder = AlertDialog.Builder(contextFragment)
            val layoutInflater : LayoutInflater = LayoutInflater.from(contextFragment)
            val promptView : View = layoutInflater.inflate(R.layout.reconnect_message, null)
            val alertButton = promptView.findViewById<Button>(R.id.button)
            builder.setView(promptView)
            val alertDialog: AlertDialog = builder.create()
            alertButton.setOnClickListener{
                // val currentFragment = requireActivity().supportFragmentManager.fragments.last()
                /*val currentFragment = HomeFragment()
                val fragmentTransaction = requireFragmentManager().beginTransaction()
                fragmentTransaction.detach(currentFragment).attach(currentFragment).commit()*/
                // requireFragmentManager().beginTransaction().replace(R.id.llHome, HomeFragment())
                alertDialog.dismiss()
                Log.d("HomeFragment", "Alert Dialog button clicked")
            }
            alertDialog.show()
        }

        forYouRecyclerView = view.findViewById(R.id.recyclerForYou)
        nearbyRecyclerView = view.findViewById(R.id.recyclerNearby)
        allStoresRecyclerView = view.findViewById(R.id.recyclerAllStores)
        allStoresRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        nearbyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        forYouRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        /*homeViewModel.stateLoadBanner.observe(viewLifecycleOwner) {
            imageMT.visibility = if (it.image) View.VISIBLE else View.GONE
        }*/

        storeAdapter = StoreAdapter(emptyList()) {
                store, user_id -> goToProductFragment(store)
        }

        allStoresRecyclerView.adapter = storeAdapter
        nearbyRecyclerView.adapter = storeAdapter
        forYouRecyclerView.adapter = storeAdapter

        storeViewModel.stores.observe(viewLifecycleOwner) { stores ->
            storeAdapter = StoreAdapter(stores) { store, user_id -> goToProductFragment(store)
            }
            allStoresRecyclerView.adapter = storeAdapter
            //nearbyRecyclerView.adapter = storeAdapter
            forYouRecyclerView.adapter = storeAdapter
        }

        storeViewModel.nearByStores.observe(viewLifecycleOwner) { stores ->
            val adapter = StoreAdapter(stores) { store, user_id -> goToProductFragment(store)
            }
            nearbyRecyclerView.adapter = adapter
        }

        requestLocationPermission(contextFragment)
        /*storeViewModel.loadNearByStores(locationManager.userLocation?.latitude ?: 0.0,
            locationManager.userLocation?.longitude ?: 0.0
        )*/

        storeViewModel.loadStores()

        val btnCamera = view.findViewById<Button>(R.id.orderConfirmation)
        btnCamera.setOnClickListener {
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
        homeViewModel.stateUpdatePhoto.observe(viewLifecycleOwner) {
            if (it) {
                // val storeImage = view.findViewById<ImageView>(R.id.storeImage)
                // storeImage.setImageBitmap(productRepository.getDeliveryProductReceivedFromCache("Product 1"))
                orderStatusViewModel.isOrderAccepted.value = false // Ocultas el botón
            } else {
                Toast.makeText(requireContext(), "The photo was not uploaded.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        val contextFragment : Context = requireContext()
        if (isGranted) {
            getUserLocation()
            // locationManager.getUserLocation(contextFragment, viewLifecycleOwner)
        } else {
            Toast.makeText(contextFragment, "Location permission denied.", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestLocationPermission(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getUserLocation()
            // locationManager.getUserLocation(contextFragment, viewLifecycleOwner)
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

        val contextFragment : Context = requireContext()

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(contextFragment)

        if (ActivityCompat.checkSelfPermission(
                contextFragment, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val location = fusedLocationClient.lastLocation.await()
                location?.let {
                    userLocation = it
                    Log.d("HomeFragment.getUserLocation", "The Location has the following" +
                            " coordinates: Lat: ${it.latitude}, Long: ${it.longitude}")
                    homeViewModel.sendUserLocation(it, contextFragment)
                    homeViewModel.storeLocation(it, contextFragment)
                    storeViewModel.loadNearByStores(it.latitude, it.longitude)
                }
            } catch (e: Exception) {
                Log.e("HomeFragment.getUserLocation", "There was an error" +
                        " retrieving the location", e)
            }
        }
    }

    private fun goToProductFragment(store: Store) {

        val productFragment = ProductFragment()
        val bundle = Bundle().apply {
            putInt("storeId", store.store_id!!) // Guardamos el ID como Int
        }
        productFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_nav_container, productFragment) // Usa el ID del contenedor en tu Activity
            .addToBackStack(null) // Para que el usuario vuelva a atrás
            .commit()
    }

    private fun requestNotificationPermission(contextFragment: Context, builder: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(contextFragment)) {
            if (ActivityCompat.checkSelfPermission(
                    contextFragment,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            notify(2, builder.build())
        }
    }

    private val notificationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Notification permission granted.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Notification permission denied.", Toast.LENGTH_LONG).show()
        }
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {
            val contextFragment : Context = requireContext()
            Toast.makeText(contextFragment, "Image taken.", Toast.LENGTH_SHORT).show()

            val data = result.data
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                photoBitmap = imageBitmap
                viewLifecycleOwner.lifecycleScope.launch {
                    homeViewModel.storePhoto(imageBitmap)
                    Log.d("HomeFragment", "The coroutine has been executed.")
                }
                // orderStatusViewModel.isOrderAccepted.value = false // Ocultas el botón
                // Glide.with(this).load(imageBitmap).into(view.findViewById(R.id.storeImage))
            } else {
                Log.d("HomeFragment.startForResult", "The image is null.")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val cameraButton = view.findViewById<LinearLayout>(R.id.CameraLayout)

        /*val contextFragment : Context = requireContext()
        // imageKitManager.initService(requireContext().applicationContext)
        // imageKitManager.getImage()
        if (!homeViewModel.isOnline(contextFragment)) {
            val builder = AlertDialog.Builder(contextFragment)
            /*
            builder.setTitle("Lost connection")
                .setMessage("You require an active connection to continue using the app. Please reconnect.")
                .setPositiveButton("Try again"){ dialog, which ->
                    if (homeViewModel.isOnline(contextFragment)) {
                        dialog.dismiss()
                    }
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()*/
        }*/

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

        val contextFragment : Context = requireContext()

        notificationManager.generateNotificationChannel(contextFragment, "User " +
                "re-visiting a place where they had ordered")

        homeViewModel.theyHadOrderedHere.observe(viewLifecycleOwner) {
            val builder = notificationManager.generateNotificationRevisitedPlace(contextFragment,"User " +
                    "re-visiting a place where they had ordered")
            requestNotificationPermission(contextFragment, builder)
        }
    }
}