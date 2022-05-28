package com.example.modul7_instagram_clone.manager.handler

import com.example.modul7_instagram_clone.model.Post

interface DBPostsHandler {
    fun onSuccess(posts: ArrayList<Post>)
    fun onError(e: Exception)
}