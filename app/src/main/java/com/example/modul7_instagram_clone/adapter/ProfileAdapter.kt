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
import com.example.modul7_instagram_clone.fragment.ProfileFragment
import com.example.modul7_instagram_clone.model.Post
import com.example.modul7_instagram_clone.utils.Utils
import com.google.android.material.imageview.ShapeableImageView

class ProfileAdapter(var fragment: ProfileFragment, var items : ArrayList<Post> ) : BaseAdapter() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_profile_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = items[position]
        if(holder is PostViewHolder){
            holder.tv_caption.text = post.caption
            setViewHeigh(holder.iv_post)
            Glide.with(fragment).load(post.postImg).into(holder.iv_post)
        }

    }

    class PostViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val iv_post: ShapeableImageView = view.findViewById(R.id.iv_post)
        val tv_caption: TextView = view.findViewById(R.id.tv_caption)
    }

    /**
     * Set ShapeableImageView height as screen width
     */
    private fun setViewHeigh(view: View) {
        val params : ViewGroup.LayoutParams = view.layoutParams
        params.height = Utils.screenSize(fragment.requireActivity().application).width/2
        view.layoutParams = params
    }
}