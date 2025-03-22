package com.example.lastbite.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.lastbite.ApiClient
import com.example.lastbite.ApiService
import com.example.lastbite.repositories.LocationRepository
import com.example.lastbite.viewmodels.LocationViewModel
import com.example.lastbite.R
import com.example.lastbite.viewmodels.SingletonSignUpViewModel
import com.example.lastbite.ViewModelFactory

class LocationActivity : AppCompatActivity() {
    private lateinit var locationViewModel: LocationViewModel
    private val signUpViewModel = SingletonSignUpViewModel.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        val btnNext: Button = findViewById(R.id.btnNext)

        // Inicializar ViewModel con Repository y ApiService
        val apiService = ApiClient.getRetrofit().create(ApiService::class.java)
        val repository = LocationRepository(apiService)


        locationViewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(
            LocationViewModel::class.java)

        val spinnerZones: Spinner = findViewById(R.id.spinnerZone)
        val spinnerAreas: Spinner = findViewById(R.id.spinnerArea)

        // Llamar a fetchZones() para obtener los datos de la API
        locationViewModel.fetchZones()
        locationViewModel.fetchAreas()

        // Observar cambios en las zonas y actualizar el Spinner
        locationViewModel.zones.observe(this) { zones ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, zones.map { it.zone_name })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerZones.adapter = adapter
        }

        fun updateAreas(zoneId: Int) {
            val spinnerAreas_: Spinner = findViewById(R.id.spinnerArea)

            // Filtrar áreas por zona seleccionada
            val filteredAreas = locationViewModel.areas.value?.filter { it.zone_id == zoneId } ?: emptyList()

            // Actualizar spinner de áreas
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filteredAreas.map { it.area_name })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAreas_.adapter = adapter
        }

        spinnerZones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedZoneId = locationViewModel.zones.value?.get(position)?.zone_id
                selectedZoneId?.let { updateAreas(it)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada si no se selecciona nada
            }
        }


        // Observar errores
        locationViewModel.errorMessage.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        // Guardar zona y área seleccionadas cuando se presiona el botón "Continuar"
        btnNext.setOnClickListener {
            val selectedZone = locationViewModel.zones.value?.get(spinnerZones.selectedItemPosition)
            val selectedArea = locationViewModel.areas.value?.firstOrNull { it.zone_id == selectedZone?.zone_id && it.area_name == spinnerAreas.selectedItem.toString() }
            Log.d("LocationActivity", "Zona seleccionada: $selectedZone")
            Log.d("LocationActivity", "Área seleccionada: $selectedArea")


            if (selectedZone != null && selectedArea != null) {
                signUpViewModel.area_id = selectedArea.area_id

                // Ir a la siguiente actividad
                Log.d("SignUpViewModel", "Teléfono: ${signUpViewModel.mobile_number}")
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Selecciona una zona y un área", Toast.LENGTH_SHORT).show()
            }
        }
    }
}