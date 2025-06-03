package com.example.lastbite.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.lastbite.entities.LocationEntity

@Dao
interface LocationDao {

    @Insert
    fun insertOne(locationEntity: LocationEntity)

    @Insert
    fun insertAll(vararg locationEntity: LocationEntity)

    @Delete
    fun delete(locationEntity: LocationEntity)

    @Query("SELECT * FROM LocationEntity")
    fun getAll(): List<LocationEntity>

}