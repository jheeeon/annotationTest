package com.example.naver.mytestapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signIn_Button.setOnClickListener { moveToMain() }
    }

    private fun moveToMain() {
        MainActivityLauncher.getInstance(this, email_TextView.getString(), firstName_TextView.getString(), lastName_TextView.getString())
                .password(password_TextView.getString())
                .address(address_TextView.getString())
                .startActivity()
    }

    private fun EditText.getString() : String {
        return this.text.toString()
    }


    private fun bcd(a:Int) {

    }

}
