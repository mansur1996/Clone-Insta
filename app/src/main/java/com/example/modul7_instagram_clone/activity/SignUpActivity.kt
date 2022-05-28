package com.example.modul7_instagram_clone.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.modul7_instagram_clone.R
import com.example.modul7_instagram_clone.manager.handler.AuthHandler
import com.example.modul7_instagram_clone.manager.AuthManager
import com.example.modul7_instagram_clone.manager.DatabaseManager
import com.example.modul7_instagram_clone.manager.PrefsManager
import com.example.modul7_instagram_clone.manager.handler.DBUserHandler
import com.example.modul7_instagram_clone.utils.Extension.toast
import com.example.modul7_instagram_clone.model.User

/**
 * In SignUpActivity, user can signup using fullname, email and password
 */

class SignUpActivity : BaseActivity() {

    var TAG = SignUpActivity::class.java.toString()
    lateinit var et_fullname : EditText
    lateinit var et_email : EditText
    lateinit var et_password : EditText
    lateinit var et_cpassword : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
    }

    private fun initViews(){
        et_fullname = findViewById(R.id.et_fullname)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        et_cpassword = findViewById(R.id.et_cpassword)

        val b_signup = findViewById<Button>(R.id.b_signun)
        b_signup.setOnClickListener {
            val fullname = et_fullname.text.toString().trim()
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            val deviceToken = PrefsManager(this).loadDeviceToken()!!

            if(email.isNotEmpty() && password.isNotEmpty()){
                val user = User(fullname, email, password, "")
                user.device_tokens.add(deviceToken)
                firebaseSignUp(user)
            }else{
                toast("Please fill all fields")
            }
            finish()
        }

        val tv_signip = findViewById<TextView>(R.id.tv_signip)
        tv_signip.setOnClickListener {
            finish()
        }
    }

    private fun firebaseSignUp(user: User) {
        showLoading(this)
        AuthManager.signUp(user.email, user.password, object : AuthHandler {
            override fun onSuccess(uid: String) {
                user.uid = uid
                storeUserToDB(user)
                toast(getString(R.string.str_signup_success))
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                toast(getString(R.string.str_signup_failed))
            }

        })
    }

    private fun storeUserToDB(user: User){
        DatabaseManager.storeUser(user, object: DBUserHandler {

            override fun onSuccess(user: User?) {
                dismissLoading()
                callMainActivity(context)
            }

            override fun onError(e: Exception) {

            }
        })
    }
}