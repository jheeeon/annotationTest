package com.example.naver.mytestapplication

import android.content.Intent
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
        val intent = Intent(baseContext, MainActivity::class.java)
        intent.putExtra("email", email_TextView.getString())
        intent.putExtra("first_name", firstName_TextView.getString())
        intent.putExtra("last_name", lastName_TextView.getString())
        intent.putExtra("password", password_TextView.getString())
        intent.putExtra("address", address_TextView.getString())

        startActivity(intent)
    }

    private fun EditText.getString() : String {
        return this.text.toString()
    }

}
