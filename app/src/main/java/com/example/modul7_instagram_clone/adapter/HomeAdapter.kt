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
import com.example.modul7_instagram_clone.manager.AuthManager
import com.example.modul7_instagram_clone.model.Post
import com.google.android.material.imageview.ShapeableImageView

class HomeAdapter(var fragment: HomeFragment, var items : ArrayList<Post> ) : BaseAdapter() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_home_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = items[position]
        if(holder is PostViewHolder){
            holder.apply {
                tv_fullname.text = post.fullname
                tv_caption.text = post.caption
                tv_time.text = post.currentDate

                iv_like.setOnClickListener {
                    if(post.isLiked){
                        post.isLiked = false
                        iv_like.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    }else{
                        post.isLiked = true
                        iv_like.setImageResource(R.drawable.ic_outline_favorite_24)
                    }

                    fragment.likeOrUnLikePost(post)
                }

                if(post.isLiked){
                    iv_like.setImageResource(R.drawable.ic_outline_favorite_24)
                }else{
                   iv_like.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }
            }

            Glide.with(fragment)
                .load(post.userImg)
                .placeholder(R.drawable.ic_baseline_person_24)
                .error(R.drawable.ic_baseline_person_24)
                .into(holder.iv_profile)

            Glide.with(fragment).load(post.postImg).into(holder.iv_post)

            val uid = AuthManager.currentUser()!!.uid
            if(uid == post.uid){
                holder.iv_more.visibility = View.VISIBLE
            }else{
                holder.iv_more.visibility = View.GONE
            }

            holder.iv_more.setOnClickListener {
                fragment.showDeleteDialog(post)
            }

        }
    }

    class PostViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val iv_post: ShapeableImageView = view.findViewById(R.id.iv_post)
        val iv_profile: ShapeableImageView = view.findViewById(R.id.iv_profile)
        val tv_fullname: TextView = view.findViewById(R.id.tv_fullname)
        val tv_caption: TextView = view.findViewById(R.id.tv_caption)

        val tv_time: TextView = view.findViewById(R.id.tv_time)
        val iv_more: ImageView = view.findViewById(R.id.iv_more)
        val iv_like: ImageView = view.findViewById(R.id.iv_like)
        val iv_share: ImageView = view.findViewById(R.id.iv_share)

    }
}