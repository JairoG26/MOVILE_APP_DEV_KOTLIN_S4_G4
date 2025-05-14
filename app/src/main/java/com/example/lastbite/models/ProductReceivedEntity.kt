package com.example.lastbite.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductReceivedEntity(

    @PrimaryKey val image_id : Int,
    @ColumnInfo(name = "image_string") val image_string : String
)
