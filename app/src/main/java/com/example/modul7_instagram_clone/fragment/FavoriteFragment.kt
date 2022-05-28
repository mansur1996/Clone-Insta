package com.example.modul7_instagram_clone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.modul7_instagram_clone.R
import com.example.modul7_instagram_clone.adapter.FavoriteAdapter
import com.example.modul7_instagram_clone.manager.AuthManager
import com.example.modul7_instagram_clone.manager.DatabaseManager
import com.example.modul7_instagram_clone.manager.handler.DBPostHandler
import com.example.modul7_instagram_clone.manager.handler.DBPostsHandler
import com.example.modul7_instagram_clone.model.Post
import com.example.modul7_instagram_clone.utils.DialogListener
import com.example.modul7_instagram_clone.utils.Utils

class FavoriteFragment : BaseFragment() {

    val TAG = FavoriteFragment::class.java.simpleName
    private lateinit var recyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        initViews(view)
        return view
    }

    fun initViews(view: View){
        recyclerView = view.findViewById(R.id.rv_favoriteFragment)
        recyclerView.layoutManager = GridLayoutManager(activity, 1)

        loadLikedFeeds()
//        refreshAdapter(loadPosts())
    }

    private fun refreshAdapter(items: ArrayList<Post>) {
        val adapter = FavoriteAdapter(this, items)
        recyclerView.adapter = adapter
    }

    fun likeOrUnLikePost(post: Post) {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.likeFeedPost(uid, post)

        loadLikedFeeds()
    }

    private fun loadLikedFeeds() {
        showLoading(requireActivity())
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadLikedFeeds(uid, object : DBPostsHandler{
            override fun onSuccess(posts: ArrayList<Post>) {
                dismissLoading()
                refreshAdapter(posts)
            }

            override fun onError(e: Exception) {
                dismissLoading()
            }

        })
    }

    private fun loadPosts() : ArrayList<Post>{
        val items = ArrayList<Post>()
        items.add(Post("", "https://images.unsplash.com/photo-1524758631624-e2822e304c36?ixlib=rb-1.2.1&ixid=MnwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHw0MHx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"))
        items.add(Post("", "https://images.unsplash.com/photo-1649562065321-bff3a06ccd31?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzNHx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"))
        items.add(Post("", "https://images.unsplash.com/photo-1649217708126-7e6cd5dbc37f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwyN3x8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"))
        items.add(Post("", "https://images.unsplash.com/photo-1649073151919-d07d6d659305?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDM1fDZzTVZqVExTa2VRfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60"))
        return items
    }


    fun showDeleteDialog(post: Post){
        Utils.dialogDouble(requireContext(), getString(R.string.str_delete_post), object :
            DialogListener {
            override fun onCallback(isChosen: Boolean) {
                if(isChosen){
                    deletePost(post)
                }
            }
        })
    }

    fun deletePost(post: Post) {
        DatabaseManager.deletePost(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                loadLikedFeeds()
            }

            override fun onError(e: Exception) {

            }
        })
    }
}