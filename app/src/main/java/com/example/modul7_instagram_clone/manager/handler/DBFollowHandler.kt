package com.example.modul7_instagram_clone.manager.handler

interface DBFollowHandler {
    fun onSuccess(isFollowed: Boolean)
    fun onError(e: Exception)
}