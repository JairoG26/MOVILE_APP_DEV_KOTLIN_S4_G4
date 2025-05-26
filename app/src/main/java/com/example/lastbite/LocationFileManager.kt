package com.example.lastbite

import android.content.Context
import android.util.Log
import java.io.File

class LocationFileManager {

    private lateinit var fileWithLocation: File
    private val fileLocationName: String = "location_stored_"
    // private val fileInternalSharedLocation: String = "/storage/emulated/0/Android/data/com.example.lastbite/files"

    private fun generateFileForStoringLocation(context: Context) {

        fileWithLocation = File(context.filesDir, fileLocationName)
        // Log.d("LocationFM.generateFile", "The path used is " + context.filesDir.absolutePath)
        // fileWithLocation = File(context.cacheDir, fileLocationName)
        Log.d("LocationFM.generateFile", "The file was generated.")
    }

    fun storeLocation(location: android.location.Location, context: Context) {

        if (!::fileWithLocation.isInitialized) {
            generateFileForStoringLocation(context)
        }

        val fileContent = "${location.latitude}, ${location.longitude} \n"
        context.openFileOutput(fileLocationName, Context.MODE_APPEND).use {
            it.write(fileContent.toByteArray())
            it.close()
        }

        Log.d("LocationFM.storeLocation", "The file was written.")
    }
}