package com.example.lastbite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lastbite.repositories.LocationRepository
import com.example.lastbite.viewmodels.LocationViewModel

class ViewModelFactory(private val repository: LocationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            return LocationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}