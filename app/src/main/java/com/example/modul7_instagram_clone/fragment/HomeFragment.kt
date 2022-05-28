package com.example.modul7_instagram_clone.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.modul7_instagram_clone.R
import com.example.modul7_instagram_clone.adapter.HomeAdapter
import com.example.modul7_instagram_clone.manager.AuthManager
import com.example.modul7_instagram_clone.manager.DatabaseManager
import com.example.modul7_instagram_clone.manager.handler.DBPostHandler
import com.example.modul7_instagram_clone.manager.handler.DBPostsHandler
import com.example.modul7_instagram_clone.model.Post
import com.example.modul7_instagram_clone.utils.DialogListener
import com.example.modul7_instagram_clone.utils.Utils
import com.example.modul7_instagram_clone.utils.Utils.dialogDouble
import java.lang.RuntimeException

class HomeFragment : BaseFragment() {
    val TAG = HomeFragment::class.java.simpleName
    private var listener : HomeListener? = null
    private lateinit var recyclerView : RecyclerView
    var feeds = ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initViews(view)
        return view
    }
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && feeds.size > 0) {
            loadMyFeeds()
        }
    }

    /**
     * onAttach is for communication of Fragments
     */

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if(context is HomeListener){
            context
        }else{
            throw RuntimeException("$context must implement HomeListener")
        }
    }

    /**
     * onDetach is for communication of Fragments
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    fun initViews(view: View){
        recyclerView = view.findViewById(R.id.rv_homefragment)
        recyclerView.layoutManager = GridLayoutManager(activity, 1)

        val iv_camera = view.findViewById<ImageView>(R.id.iv_camera)
        iv_camera.setOnClickListener {
            listener!!.scrollToUpload()
        }

        loadMyFeeds()
//        refreshAdapter(loadPosts())
    }

    private fun refreshAdapter(items : ArrayList<Post>){
        val adapter = HomeAdapter(this, items)
        recyclerView.adapter = adapter
    }

    private fun loadPosts() : ArrayList<Post>{
        val items = ArrayList<Post>()
        items.add(Post("","https://images.unsplash.com/photo-1649562065321-bff3a06ccd31?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzNHx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"))
        items.add(Post("", "https://images.unsplash.com/photo-1524758631624-e2822e304c36?ixlib=rb-1.2.1&ixid=MnwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHw0MHx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"))
        items.add(Post("", "https://images.unsplash.com/photo-1649073151919-d07d6d659305?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDM1fDZzTVZqVExTa2VRfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60"))
        items.add(Post("", "https://images.unsplash.com/photo-1649217708126-7e6cd5dbc37f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwyN3x8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"))
        return items
    }

    private fun loadMyFeeds() {
        showLoading(requireActivity())
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadFeeds(uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Post>) {
                dismissLoading()
                feeds.clear()
                feeds.addAll(posts)
                refreshAdapter(feeds)
            }

            override fun onError(e: Exception) {
                dismissLoading()
            }
        })
    }

    fun likeOrUnLikePost(post: Post) {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.likeFeedPost(uid, post)
    }

    fun showDeleteDialog(post: Post){
        Utils.dialogDouble(requireContext(), getString(R.string.str_delete_post), object : DialogListener{
            override fun onCallback(isChosen: Boolean) {

            }
        })
    }

    fun deletePost(post: Post) {
        DatabaseManager.deletePost(post, object : DBPostHandler {
            override fun onSuccess(post: Post) {
                loadMyFeeds()
            }

            override fun onError(e: Exception) {

            }
        })
    }

    /**
     * This interface is created for communication with UploadFragment
     */
    interface HomeListener {
        fun scrollToUpload()
    }

}

