package com.example.lastbite

import android.graphics.Bitmap
import android.util.Log
import android.util.LruCache

class ProductReceivedLRUCacheManager {

    private lateinit var memoryCache: LruCache<String, Bitmap>

    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 8

    private var count : Int = 0

    fun init() {

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }

        Log.d("ProductReceivedLRUCache", "The LRU cache has been initialized.")
    }

    fun addBitmapToMemoryCache(bitmap: Bitmap) {

        memoryCache.put("Product ${count+1}", bitmap)
        Log.d("ProductReceivedLRUCache", "The image has been stored.")
    }

    fun getBitmapFromMemoryCache(key: String): Bitmap {
        return memoryCache.get(key)
    }
}