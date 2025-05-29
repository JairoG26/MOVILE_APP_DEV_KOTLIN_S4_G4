package com.example.lastbite

import android.content.Context
import com.imagekit.android.ImageKit

class ImageKitManager {

    fun initService(context: Context) {
        ImageKit.init(
            context = context,
            publicKey = "public_Qi9W89As3j4KFyVJVZpDvsmAfQM=",
            urlEndpoint = "https://ik.imagekit.io/lastbite/",
            // transformationPosition = TransformationPosition.PATH
        )
    }

    fun getImage() {
        ImageKit.getInstance()
            .url(
                src = "https://ik.imagekit.io/lastbite/banner",
                // transformationPosition = TransformationPosition.PATH
            )
            /*.setResponsive(
                view = binding.image,*/
            .create()
    }
}