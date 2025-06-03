package com.example.lastbite

import android.content.Context
import androidx.room.Room

object LocalDatabase {

    lateinit var db : AppDatabase

    fun init(appContext : Context) {
        db = Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "LastBite-LocalDB"
        ).build()
    }

}