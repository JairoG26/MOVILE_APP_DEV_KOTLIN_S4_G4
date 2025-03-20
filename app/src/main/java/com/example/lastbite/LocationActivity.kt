package com.example.lastbite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
    private lateinit var viewModel: LocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        // Inicializar ViewModel
        val apiService = ApiClient.getRetrofit().create(ApiService::class.java)
        val repository = LocationRepository(apiService)
        viewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(LocationViewModel::class.java)

        val spinnerZones: Spinner = findViewById(R.id.spinnerZone)
        val spinnerAreas: Spinner = findViewById(R.id.spinnerArea)

        // Observar cambios en zonas y actualizar spinner
        viewModel.zones.observe(this) { zones ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, zones)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerZones.adapter = adapter
        }

        viewModel.areas.observe(this) { areas ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, areas)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAreas.adapter = adapter
        }

        viewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        // Llamar a la API cuando la actividad se crea
        viewModel.fetchZones()
        viewModel.fetchAreas()
    }
}