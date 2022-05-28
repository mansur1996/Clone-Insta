package com.example.modul7_instagram_clone.manager.handler

import com.example.modul7_instagram_clone.model.User

interface DBUserHandler {
    fun onSuccess(user: User? = null)
    fun onError(e: Exception)
}