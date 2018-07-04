package com.example.naver.mytestapplication


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.naver.annotation.Launcher
import kotlinx.android.synthetic.main.activity_main.*

@Launcher
class MainActivity: AppCompatActivity() {

    lateinit var email: String
    var password: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        exit_button.setOnClickListener({ finish() })
        email = intent.getStringExtra("email")
        password = intent.getStringExtra("password") ?: ""
        user_email.text = email
        user_password.text = password
    }

}