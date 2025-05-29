package com.example.lastbite

import android.util.Log
import android.util.SparseIntArray
import com.example.lastbite.models.StoreCount

class StoreCountSparseArrayCacheManager {

    private val cache = SparseIntArray()
    private var count : Int = 0

    fun store(storeCount: StoreCount) {

        storeCount.store_id?.let { cache.put(count+1, it) }
        Log.d("SCSparseArrayCacheManager", "A new registry has been added.")
    }

    fun getRegistry(key: Int) : Int {
        return cache.get(key)
    }
}