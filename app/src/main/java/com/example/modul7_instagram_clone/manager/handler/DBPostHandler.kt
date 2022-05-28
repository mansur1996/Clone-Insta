package com.example.modul7_instagram_clone.manager.handler

import com.example.modul7_instagram_clone.model.Post

interface DBPostHandler{
    fun onSuccess(post: Post)
    fun onError(e: Exception)
}