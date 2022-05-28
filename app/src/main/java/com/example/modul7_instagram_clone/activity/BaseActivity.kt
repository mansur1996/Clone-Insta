package com.example.modul7_instagram_clone.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import com.example.modul7_instagram_clone.R

/**
 * BaseActivity is parent for all Activities
 */

open class BaseActivity : AppCompatActivity() {

    lateinit var context: Context
    var progressDialog : AppCompatDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setupStatusBar(){
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }

    fun showLoading(activity: Activity?) {
        if (activity == null) return

        if (progressDialog != null && progressDialog!!.isShowing) {
//            progressDialog.dismiss();
        } else {
            progressDialog = AppCompatDialog(activity, R.style.CustomDialog)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog!!.setContentView(R.layout.custom_progress_dialog)

            val iv_progress = progressDialog!!.findViewById<ImageView>(R.id.iv_progress)
            val animationDrawable = iv_progress!!.drawable as AnimationDrawable
            animationDrawable.start()
            if (!activity.isFinishing) progressDialog!!.show()
        }
    }

    protected fun dismissLoading() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    fun callMainActivity(context: Context){
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun callSignInActivity(context: Context){
        val intent = Intent(context, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

}