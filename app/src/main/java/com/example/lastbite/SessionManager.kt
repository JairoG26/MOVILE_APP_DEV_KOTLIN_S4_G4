package com.example.lastbite

import com.example.lastbite.models.User

object SessionManager {
    var currentUser: User? = null

    fun saveUser(user: User) {
        currentUser = user
    }

    fun getUser(): User? {
        return currentUser
    }

    fun logout() {
        currentUser = null
    }
}