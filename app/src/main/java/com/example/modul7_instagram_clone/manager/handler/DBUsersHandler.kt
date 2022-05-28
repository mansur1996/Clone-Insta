package com.example.modul7_instagram_clone.manager.handler

import com.example.modul7_instagram_clone.model.User

interface DBUsersHandler {
    fun onSuccess(users: ArrayList<User>)
    fun onError(e: Exception)
}