package com.example.naver.mytestapplication


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.naver.annotation.IntentExtra
import com.example.naver.annotation.Launcher
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.full.*

class MainActivity: AppCompatActivity() {

    lateinit var email: String
    lateinit var firstName: String
    lateinit var lastName: String
    var password: String? = null
    var address: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()

        user_email.text = email
        user_first_name.text = firstName
        user_last_name.text = lastName

        user_password.text = password ?: ""
        user_address.text = address ?: ""

        exit_button.setOnClickListener{ finish() }
    }

    private fun initData() {
        email = intent.getStringExtra("email")
        firstName = intent.getStringExtra("firstName")
        lastName = intent.getStringExtra("lastName")
        password = intent.getStringExtra("password")
        address = intent.getStringExtra("address")




//        intent.getSerializableExtra("activityParser")  =>  MainActivityParser(this).parseAll()



//        val parserClass = intent.getSerializableExtra("activityParser") as Class<*>
//        parserClass.getDeclaredMethod("parseAll").invoke(parserClass.getConstructor(this::class.java)?.newInstance(this))


//        val parserClass = intent.getSerializableExtra("activityParser") as Class<*>?
//        val kClass = parserClass?.kotlin ?: run {
//            Log.e(this::class.simpleName, "activityParser not found")
//            return
//        }
//
//        kClass.declaredMemberProperties.forEach { Log.d("::::::::::declaredMemberProperties::::::::::", "name = ${it.name}") }
//        kClass.declaredFunctions.forEach { Log.d("::::::::::declaredFunctions::::::::::", "\nname = ${it.name}   \nreturnType = ${it.returnType}   \nparams = ${it.parameters}") }
//
//        kClass.declaredFunctions.find { it.name == "parseAll" }?.call(kClass.primaryConstructor?.call(this)) ?: run {
//            Log.e(this::class.simpleName, "parseAll() not found")
//            return
//        }
    }
}