package com.example.modul7_instagram_clone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.modul7_instagram_clone.R
import com.example.modul7_instagram_clone.fragment.HomeFragment
import com.example.modul7_instagram_clone.fragment.SearchFragment
import com.example.modul7_instagram_clone.model.Post
import com.example.modul7_instagram_clone.model.User
import com.google.android.material.imageview.ShapeableImageView

class SearchAdapter(var fragment: SearchFragment, var items : ArrayList<User> ) : BaseAdapter() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = items[position]
        if(holder is UserViewHolder){
            holder.apply {
                tv_fullname.text = user.fullname
                tv_email.text = user.email

                tv_follow.setOnClickListener {
                    if(!user.isFollowed){
                        tv_follow.text = fragment.getString(R.string.str_following)
                    }else{
                        tv_follow.text = fragment.getString(R.string.str_follow)
                    }
                    fragment.followOrUnfollow(user)
                }

                if(!user.isFollowed){
                    tv_follow.text = fragment.getString(R.string.str_follow)
                }else{
                    tv_follow.text = fragment.getString(R.string.str_follow)
                }
            }

            Glide.with(fragment).load(user.userImg)
                .placeholder(R.drawable.ic_baseline_person_24)
                .error(R.drawable.ic_baseline_person_24)
                .into(holder.iv_profile)
        }

    }

    class UserViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val tv_fullname: TextView = view.findViewById(R.id.tv_fullname)
        val tv_email: TextView = view.findViewById(R.id.tv_email)
        val iv_profile: ShapeableImageView = view.findViewById(R.id.iv_profile)
        val tv_follow: TextView = view.findViewById(R.id.tv_follow)

    }
}