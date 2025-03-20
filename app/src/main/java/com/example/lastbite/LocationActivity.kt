package com.example.lastbite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

class LocationActivity : AppCompatActivity() {
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        val btnNext: Button = findViewById(R.id.btnNext)

        // Inicializar ViewModel con Repository y ApiService
        val apiService = ApiClient.getRetrofit().create(ApiService::class.java)
        val repository = LocationRepository(apiService)
        locationViewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(LocationViewModel::class.java)

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
                selectedZoneId?.let { updateAreas(it) }
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
            val selectedArea = locationViewModel.filteredAreas.value?.get(spinnerAreas.selectedItemPosition)

            if (selectedZone != null && selectedArea != null) {
                signUpViewModel.area_id = selectedArea.area_id

                // Ir a la siguiente actividad
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Selecciona una zona y un área", Toast.LENGTH_SHORT).show()
            }
        }
    }
}