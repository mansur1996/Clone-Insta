package com.example.modul7_instagram_clone.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.modul7_instagram_clone.R
import com.example.modul7_instagram_clone.manager.handler.AuthHandler
import com.example.modul7_instagram_clone.manager.AuthManager
import com.example.modul7_instagram_clone.manager.DatabaseManager
import com.example.modul7_instagram_clone.manager.PrefsManager
import com.example.modul7_instagram_clone.manager.handler.DBUserHandler
import com.example.modul7_instagram_clone.model.User
import com.example.modul7_instagram_clone.utils.Extension.toast
import java.lang.Exception

/**
 * In SignUpActivity, user can login using email, password
 */
class SignInActivity : BaseActivity() {
    val TAG = SignInActivity::class.java.simpleName
    lateinit var et_email: EditText
    lateinit var et_password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initViews()
    }

    private fun initViews() {
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        val b_signin = findViewById<Button>(R.id.b_signin)
        b_signin.setOnClickListener {
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty())
                firebaseSignIn(email, password)
        }
        val tv_signup = findViewById<TextView>(R.id.tv_signup)
        tv_signup.setOnClickListener { callSignUpActivity() }
    }

    private fun firebaseSignIn(email: String, password: String) {
        showLoading(this)
        AuthManager.signIn(email, password, object : AuthHandler {
            override fun onSuccess(uid: String) {
                dismissLoading()
                toast(getString(R.string.str_signin_success))
                storeDeviceTokenToUser()
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                toast(getString(R.string.str_signin_failed))
            }
        })
    }

    private fun storeDeviceTokenToUser() {
        val deviceToken = PrefsManager(this).loadDeviceToken()
        var uid = AuthManager.currentUser()!!.uid
        DatabaseManager.addMyDeviceToken(uid, deviceToken, object : DBUserHandler {
            override fun onSuccess(user: User?) {
                callMainActivity()
            }

            override fun onError(e: Exception) {
                callMainActivity()
            }

        })

    }

    private fun callSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun callMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}