package com.example.lastbite.repositories

import android.content.Context
import android.media.Image
import com.example.lastbite.ImageKitManager

class BannerRepository {

    private val imageKitManager = ImageKitManager()

    fun load(context: Context, callback: (Boolean, Image) -> Unit) {

        imageKitManager.initService(context)
        imageKitManager.getImage()

    }

}