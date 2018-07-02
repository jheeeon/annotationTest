package com.example.naver.mytestapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

//@Launcher
class MainActivity: AppCompatActivity() {

//    @set:IntentExtra
    lateinit var email: String

//    @set:IntentExtra
    lateinit var password: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        email = intent.getStringExtra("email")
        password = intent.getStringExtra("password")
        user_email.text = email
        user_password.text = password
        exit_button.setOnClickListener({ finish() })
    }
}