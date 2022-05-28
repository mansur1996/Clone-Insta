package com.example.modul7_instagram_clone.fragment

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.modul7_instagram_clone.R
import com.example.modul7_instagram_clone.adapter.ProfileAdapter
import com.example.modul7_instagram_clone.manager.AuthManager
import com.example.modul7_instagram_clone.manager.DatabaseManager
import com.example.modul7_instagram_clone.manager.StorageManager
import com.example.modul7_instagram_clone.manager.handler.DBPostsHandler
import com.example.modul7_instagram_clone.manager.handler.DBUserHandler
import com.example.modul7_instagram_clone.manager.handler.DBUsersHandler
import com.example.modul7_instagram_clone.manager.handler.StorageHandler
import com.example.modul7_instagram_clone.model.Post
import com.example.modul7_instagram_clone.model.User
import com.example.modul7_instagram_clone.utils.Logger
import com.google.android.material.imageview.ShapeableImageView
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter

/**
 * In ProfileFragment, posts that user uploaded can be seen and user is able to change his/her profile photo
 */

class ProfileFragment : BaseFragment() {
    val TAG = ProfileFragment::class.java.simpleName
    lateinit var rv_profile: RecyclerView
    lateinit var tv_fullname: TextView
    lateinit var tv_email: TextView
    lateinit var tv_posts: TextView
    lateinit var tv_followers: TextView
    lateinit var tv_following: TextView
    lateinit var iv_profile: ShapeableImageView


    var pickedPhoto: Uri? = null
    var allPhotos = ArrayList<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        initViews(view)
        return view
    }

    fun initViews(view: View){
        rv_profile = view.findViewById(R.id.rv_profile)
        rv_profile.setLayoutManager(GridLayoutManager(activity, 2))
        tv_fullname = view.findViewById(R.id.tv_fullname)
        tv_email = view.findViewById(R.id.tv_email)
        tv_posts = view.findViewById(R.id.tv_posts)
        tv_followers = view.findViewById(R.id.tv_followers)
        tv_following = view.findViewById(R.id.tv_following)
        iv_profile = view.findViewById(R.id.iv_profile)

        val iv_logout = view.findViewById<ImageView>(R.id.iv_logout)
        iv_logout.setOnClickListener {
            AuthManager.signOut()
            callSignInActivity(requireActivity())
        }
        iv_profile.setOnClickListener {
            pickFishBunPhoto()
        }

        loadUserInfo()
        loadMyPosts()
        loadMyFollowing()
        loadMyFollowers()
    }

    private fun loadMyFollowers(){
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadFollowers(uid, object : DBUsersHandler{
            override fun onSuccess(users: ArrayList<User>) {
                tv_followers.text = users.size.toString()
            }

            override fun onError(e: Exception) {

            }
        })
    }

    private fun loadMyFollowing(){
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadFollowing(uid, object : DBUsersHandler {
            override fun onSuccess(users: ArrayList<User>) {
                tv_following.text = users.size.toString()
            }

            override fun onError(e: Exception) {

            }
        })
    }

    private fun loadMyPosts(){
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadPosts(uid, object: DBPostsHandler{
            override fun onSuccess(posts: ArrayList<Post>) {
                tv_posts.text = posts.size.toString()
                refreshAdapter(posts)
            }

            override fun onError(e: Exception) {
                TODO("Not yet implemented")
            }
        })
    }

    /**
     * Pick photo using FishBun library
     */

    private fun pickFishBunPhoto() {
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .setSelectedImages(allPhotos)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }

    private val photoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            allPhotos = it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
            pickedPhoto = allPhotos[0]
            uploadPickedPhoto()
        }
    }

    private fun uploadPickedPhoto() {
        StorageManager.uploadUserPhoto(pickedPhoto!!, object : StorageHandler{
            override fun onSuccess(userImg: String) {
                DatabaseManager.updateUserImage(userImg)
                iv_profile.setImageURI(pickedPhoto)
            }

            override fun onError(exception: Exception?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun loadUserInfo() {
        DatabaseManager.loadUser(AuthManager.currentUser()!!.uid, object : DBUserHandler {
            override fun onSuccess(user: User?) {
                if (user != null) {
                    showUserInfo(user)
                }
            }

            override fun onError(e: Exception) {

            }
        })
    }

    private fun showUserInfo(user: User){
        tv_fullname.text = user.fullname
        tv_email.text = user.email

        Glide.with(this).load(user.userImg)
            .placeholder(R.drawable.ic_baseline_person_24)
            .error(R.drawable.ic_baseline_person_24)
            .into(iv_profile)
    }

    private fun refreshAdapter(items : ArrayList<Post>){
        val adapter = ProfileAdapter(this, items)
        rv_profile.adapter = adapter
    }

//    private fun loadPosts() : ArrayList<Post>{
//        val items = ArrayList<Post>()
//        items.add(Post("https://images.unsplash.com/photo-1649217708126-7e6cd5dbc37f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwyN3x8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"))
//        items.add(Post("https://images.unsplash.com/photo-1649562065321-bff3a06ccd31?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwzNHx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"))
//        items.add(Post("https://images.unsplash.com/photo-1649073151919-d07d6d659305?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDM1fDZzTVZqVExTa2VRfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60"))
//        items.add(Post("https://images.unsplash.com/photo-1524758631624-e2822e304c36?ixlib=rb-1.2.1&ixid=MnwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHw0MHx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"))
//        items.add(Post("https://images.unsplash.com/photo-1649217708126-7e6cd5dbc37f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwyN3x8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"))
//        items.add(Post("https://images.unsplash.com/photo-1649073151919-d07d6d659305?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDM1fDZzTVZqVExTa2VRfHxlbnwwfHx8fA%3D%3D&auto=format&fit=crop&w=500&q=60"))
//        items.add(Post("https://images.unsplash.com/photo-1524758631624-e2822e304c36?ixlib=rb-1.2.1&ixid=MnwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHw0MHx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"))
//        return items
//    }

}