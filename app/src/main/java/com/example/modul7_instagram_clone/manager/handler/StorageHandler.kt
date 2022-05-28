package com.example.modul7_instagram_clone.manager.handler

interface StorageHandler {
    fun onSuccess(imgUrl: String)
    fun onError(exception: Exception?)
}