package com.example.lastbite.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationEntity(

    @PrimaryKey val location_id : Int,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "count") val count: Int?
)
