package com.example.modul7_instagram_clone.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

object Extension {
    fun Activity.toast(msg : String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.toast(msg : String){
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }
}