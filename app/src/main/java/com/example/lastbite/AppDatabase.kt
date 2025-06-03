package com.example.lastbite

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lastbite.daos.LocationDao
import com.example.lastbite.daos.OrderDao
import com.example.lastbite.entities.LocationEntity
import com.example.lastbite.entities.OrderEntity
import com.example.lastbite.entities.ProductReceivedEntity

@Database(entities = [OrderEntity::class, LocationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun orderDao() : OrderDao
    abstract fun locationDao() : LocationDao
}