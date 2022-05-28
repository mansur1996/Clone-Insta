package com.example.modul7_instagram_clone.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.modul7_instagram_clone.R
import com.example.modul7_instagram_clone.adapter.SearchAdapter
import com.example.modul7_instagram_clone.manager.AuthManager
import com.example.modul7_instagram_clone.manager.DatabaseManager
import com.example.modul7_instagram_clone.manager.handler.DBFollowHandler
import com.example.modul7_instagram_clone.manager.handler.DBUserHandler
import com.example.modul7_instagram_clone.manager.handler.DBUsersHandler
import com.example.modul7_instagram_clone.model.User

/**
 * inSearchActivity, all registered users can be found by searching keyword and followed.
 */

class SearchFragment : BaseFragment() {

    val TAG = SearchFragment::class.java.simpleName
    lateinit var rv_home : RecyclerView
    var items = ArrayList<User>()
    var users = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View){
        rv_home = view.findViewById(R.id.rv_searchFragment)
        rv_home.layoutManager = GridLayoutManager(activity, 1)
        val et_search = view.findViewById<EditText>(R.id.et_search)
        et_search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val keyword = p0.toString().trim()
                usersByKeyword(keyword)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        loadUsers()
//        refreshAdapter(items)

    }

    private fun refreshAdapter(items: ArrayList<User>) {
        val adapter = SearchAdapter(this, items)
        rv_home.adapter = adapter
    }

    private fun usersByKeyword(keyword: String) {
        if(keyword.isEmpty()){
            refreshAdapter(items)
        }

        users.clear()
        for(user in items){
            if(user.fullname.startsWith(keyword))
                users.add(user)
        }
        refreshAdapter(users)
    }

    private fun loadUsers(): ArrayList<User> {
        DatabaseManager.loadUsers(object : DBUsersHandler{
            override fun onSuccess(users: ArrayList<User>) {
                items.clear()
                items.addAll(users)
                refreshAdapter(items)
            }

            override fun onError(e: Exception) {

            }

        })
        return items
    }

    private fun mergedUsers(uid:String, users: ArrayList<User>, following: ArrayList<User>): ArrayList<User> {
        val items = ArrayList<User>()
        for (u in users){
            val user = u
            for(f in following){
                if(u.uid == f.uid){
                    user.isFollowed = true
                    break
                }
            }
            if(uid != user.uid){
                items.add(user)
            }
        }
        return items
    }

    fun followOrUnfollow(to: User) {
        val uid = AuthManager.currentUser()!!.uid
        if (!to.isFollowed) {
            followUser(uid, to)
        } else {
            unFollowUser(uid, to)
        }
    }

    private fun followUser(uid: String, to: User) {
        DatabaseManager.loadUser(uid, object : DBUserHandler {
            override fun onSuccess(me: User?) {
                DatabaseManager.followUser(me!!, to, object : DBFollowHandler {
                    override fun onSuccess(isFollowed: Boolean) {
                        to.isFollowed = true
                        //DatabaseManager.storePostsToMyFeed(uid, to)
                    }

                    override fun onError(e: Exception) {
                    }
                })
            }
            override fun onError(e: Exception) {

            }
        })
    }

    private fun unFollowUser(uid: String, to: User) {
        DatabaseManager.loadUser(uid, object : DBUserHandler {
            override fun onSuccess(me: User?) {
                DatabaseManager.unFollowUser(me!!, to, object : DBFollowHandler {
                    override fun onSuccess(isFollowed: Boolean) {
                        to.isFollowed = false
                        //DatabaseManager.removePostsFromMyFeed(uid, to)
                    }

                    override fun onError(e: Exception) {

                    }
                })
            }

            override fun onError(e: Exception) {

            }
        })
    }

//    private fun loadUsers() : ArrayList<User> {
//        items = ArrayList()
//        items.add(User("Kamron", "kamron.com@gmail.com"))
//        items.add(User("Odilbek", "odilbek.com@gmail.com"))
//        items.add(User("Feruzbek", "feruzbek.com@gmail.com"))
//        items.add(User("Shaxriyor", "shaxriyor.com@gmail.com"))
//        items.add(User("Bog'ibek", "bog'ibek.com@gmail.com"))
//        items.add(User("Yaxyo", "yaxyo.com@gmail.com"))
//        items.add(User("Elmurod", "elmurod.com@gmail.com"))
//        items.add(User("Jamshid", "jamshid.com@gmail.com"))
//        items.add(User("Umidjon", "umidjon.com@gmail.com"))
//        return items
//    }

}