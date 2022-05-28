package com.example.modul7_instagram_clone.manager.handler

import java.lang.Exception

interface AuthHandler {
    fun onSuccess(uid : String)
    fun onError(exception: Exception?)
}
