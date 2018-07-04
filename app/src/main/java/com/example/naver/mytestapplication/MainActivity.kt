package com.example.naver.mytestapplication


import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v7.app.AppCompatActivity
import com.example.naver.annotation.IntentExtra
import com.example.naver.annotation.Launcher
import kotlinx.android.synthetic.main.activity_main.*

@Launcher
class MainActivity: AppCompatActivity() {

    @IntentExtra @NonNull
    lateinit var email: String
    @IntentExtra @NonNull
    lateinit var firstName: String
    @IntentExtra @NonNull
    lateinit var lastName: String
    @IntentExtra var password: String? = ""
    @IntentExtra var address: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        exit_button.setOnClickListener{ finish() }

        user_email.text = email
        user_first_name.text = firstName
        user_last_name.text = lastName

        user_password.text = password
        user_address.text = address
    }

    private fun inject() {
        val parserClass = intent.getSerializableExtra("activityParser") as Class<*>
        parserClass.getDeclaredMethod("parseAll").invoke(parserClass.getConstructor(this::class.java)?.newInstance(this))
    }

}