package com.example.naver.mytestapplication


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.naver.annotation.IntentExtra
import com.example.naver.annotation.Launcher
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.full.*

@Launcher
class MainActivity: AppCompatActivity() {

    @IntentExtra
    lateinit var email: String
    @IntentExtra
    lateinit var firstName: String
    @IntentExtra
    lateinit var lastName: String
    @IntentExtra
    var password: String? = null
    @IntentExtra
    var address: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user_email.text = email
        user_first_name.text = firstName
        user_last_name.text = lastName

        user_password.text = password ?: ""
        user_address.text = address ?: ""

        exit_button.setOnClickListener{ finish() }
    }

    private fun inject() {
//        intent.getSerializableExtra("activityParser")  =>  MainActivityParser(this).parseAll()

        val parserClass = intent.getSerializableExtra("activityParser") as Class<*>
//        parserClass.getDeclaredMethod("parseAll").invoke(parserClass.getConstructor(this::class.java)?.newInstance(this))


        val kClass = parserClass.kotlin
        kClass.declaredMemberProperties.forEach { Log.d("::::::::::declaredMemberProperties::::::::::", "name = ${it.name}") }
        kClass.declaredFunctions.forEach { Log.d("::::::::::declaredFunctions::::::::::", "\nname = ${it.name}   \nreturnType = ${it.returnType}   \nparams = ${it.parameters}") }


        kClass.declaredFunctions.find { it.name == "parseAll" }!!.call(kClass.primaryConstructor!!.call(this))
    }
}