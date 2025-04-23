package com.example.lastbite

import android.content.ContentValues
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
import com.example.lastbite.models.Product
import com.example.lastbite.viewmodels.ProductViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class CreateProductFragment : Fragment() {

    private val productViewModel = ProductViewModel()
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null
    private lateinit var etImageUrl: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_create_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etName = view.findViewById<EditText>(R.id.etProductName)
        val etPrice = view.findViewById<EditText>(R.id.etPrice)
        val etDetail = view.findViewById<EditText>(R.id.etDetail)
        val etType = view.findViewById<EditText>(R.id.etType)
        val etScore = view.findViewById<EditText>(R.id.etScore)
        etImageUrl = view.findViewById<EditText>(R.id.etImageUrl)
        val btnSave = view.findViewById<Button>(R.id.btnSaveProduct)

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && photoUri != null) {
                view.findViewById<ImageView>(R.id.ivPreview).setImageURI(photoUri)
                // Lanzar coroutine
                lifecycleScope.launch {
                    try {
                        val imageUrl = uploadImageToFirebaseAsync(photoUri!!)
                        etImageUrl.setText(imageUrl)
                        Toast.makeText(requireContext(), "Imagen subida", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }
            }
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val price = etPrice.text.toString().toFloatOrNull() ?: 0f
            val detail = etDetail.text.toString()
            val type = etType.text.toString()
            val score = etScore.text.toString().toFloatOrNull() ?: 0f
            val image = etImageUrl.text.toString()
            val storeId = StoreManager.storeId

            val newProduct = Product(
                product_id = null,
                name = name,
                unit_price = price,
                detail = detail,
                product_type = type,
                score = score,
                image = image,
                store_id = storeId!!
            )

            productViewModel.createProduct(newProduct)

            Toast.makeText(requireContext(), "Producto creado", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
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
        return requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
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