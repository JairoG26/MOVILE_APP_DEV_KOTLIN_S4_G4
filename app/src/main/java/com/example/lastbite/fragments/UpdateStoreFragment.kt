package com.example.lastbite.fragments

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.lastbite.R
import com.example.lastbite.StoreManager
import com.example.lastbite.models.Store
import com.example.lastbite.viewmodels.ProductViewModel
import com.example.lastbite.viewmodels.StoreViewModel
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class UpdateStoreFragment : Fragment() {
    private val prdViewModel = ProductViewModel()
    private val storeViewModel = StoreViewModel()
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null
    private lateinit var etImageUrl: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_update_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etName = view.findViewById<EditText>(R.id.etStoreName)
        val etNit = view.findViewById<EditText>(R.id.etNit)
        val etAddress = view.findViewById<EditText>(R.id.etAddress)
        val etLongitude = view.findViewById<EditText>(R.id.etLongitude)
        val etLatitude = view.findViewById<EditText>(R.id.etLatitude)
        etImageUrl = view.findViewById(R.id.etImageUrl) // Inicializado antes de usarse
        val btnSave = view.findViewById<Button>(R.id.btnSaveStore)

        // Cargar SharedPreferences
        val prefs = requireContext().getSharedPreferences("store_prefs", Context.MODE_PRIVATE)
        val lastImageUrl = prefs.getString("last_image_url", null)
        lastImageUrl?.let {
            etImageUrl.setText(it)
            Toast.makeText(requireContext(), "Imagen previa cargada", Toast.LENGTH_SHORT).show()
        }

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && photoUri != null) {
                view.findViewById<ImageView>(R.id.ivPreview).setImageURI(photoUri)
                lifecycleScope.launch {
                    try {
                        val imageUrl = uploadImageToFirebaseAsync(photoUri!!)
                        etImageUrl.setText(imageUrl)

                        // Guardar en SharedPreferences
                        prefs.edit().putString("last_image_url", imageUrl).apply()

                        Toast.makeText(requireContext(), "Imagen subida", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }
            }
        }

        btnSave.setOnClickListener {
            if (!prdViewModel.hayConexion(requireContext())) {
                Toast.makeText(requireContext(), "You need to have internet to do this", Toast.LENGTH_SHORT).show()
            } else if (etImageUrl.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Image is blank or is not uploaded yet", Toast.LENGTH_SHORT).show()
            } else {
                val name = etName.text.toString()
                val nit = etNit.text.toString()
                val address = etAddress.text.toString()
                val longitude = etLongitude.text.toString().toFloat()
                val latitude = etLatitude.text.toString().toFloat()
                val image = etImageUrl.text.toString()
                val storeId = StoreManager.storeId

                val newStore = Store(
                    store_id = storeId,
                    nit = nit,
                    name = name,
                    address = address,
                    longitude = longitude,
                    latitude = latitude,
                    logo = image,
                    opens_at = "08:00:00",
                    closes_at = "20:00:00"
                )

                storeViewModel.updateStore(storeId!!, newStore)
                Toast.makeText(requireContext(), "Store updated", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            }
        }

        val btnTakePhoto = view.findViewById<Button>(R.id.btnTakePhoto)
        btnTakePhoto.setOnClickListener {
            photoUri = createImageUri()
            photoUri?.let { uri ->
                takePictureLauncher.launch(uri)
            }
        }
    }

    private fun createImageUri(): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }

    private suspend fun uploadImageToFirebaseAsync(imageUri: Uri): String {
        return withContext(Dispatchers.IO) {
            val storageReference = FirebaseStorage.getInstance().reference
            val fileName = UUID.randomUUID().toString()
            val imageRef = storageReference.child("images/$fileName.jpg")

            imageRef.putFile(imageUri).await()
            imageRef.downloadUrl.await().toString()
        }
    }
}
