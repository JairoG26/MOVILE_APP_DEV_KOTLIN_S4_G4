package com.example.lastbite

import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)
    }
}