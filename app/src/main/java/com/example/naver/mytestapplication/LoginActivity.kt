package com.example.naver.mytestapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email_sign_in_button.setOnClickListener { moveToMain() }
    }

    private fun moveToMain() {
        MainActivityLauncher.getInstance(this, email.text.toString())
                .password(password.text.toString())
                .startActivity()
    }
}
