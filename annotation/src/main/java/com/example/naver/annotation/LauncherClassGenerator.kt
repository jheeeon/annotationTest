package com.example.naver.annotation

import android.app.Activity
import android.content.Intent
import com.squareup.kotlinpoet.*

const val LAUNCHER_SUFFIX = "Launcher"

internal object LauncherClassGenerator {

    fun generateFileSpec(activityInfo: ActivityInfo): FileSpec {
        val packageName = activityInfo.packageName
        val activityName = activityInfo.activityName
        val launcherClassName = activityName + LAUNCHER_SUFFIX
        val launcherClass = ClassName(packageName, launcherClassName)

        return FileSpec.builder(packageName, launcherClassName)
                .addType(TypeSpec.classBuilder(launcherClassName)
                        .primaryConstructor(FunSpec.constructorBuilder()
                                .addParameter("activity", Activity::class)
                                .addParameter("email", String::class)
                                .build())
                        .addProperty(PropertySpec.varBuilder("activity", Activity::class).initializer("activity").build())
                        .addProperty(PropertySpec.varBuilder("email", String::class).initializer("email").build())
                        .addProperty(PropertySpec.builder("intent", Intent::class)
                                .initializer("Intent(activity, MainActivity::class.java).putExtra(%S, email)", "email")
                                .build())
                        .addFunction(FunSpec.builder("startActivity")
                                .addStatement("activity.startActivity(intent)")
                                .build())
                        .addProperty(PropertySpec.varBuilder("password", String::class)
                                .initializer("%S", "")
                                .setter(FunSpec.setterBuilder()
                                        .addParameter("value", String::class)
                                        .addStatement("field = value")
                                        .addStatement("intent.putExtra(%S, value)", "password")
                                        .build())
                                .build())
                        .addFunction(FunSpec.builder("password")
                                .addParameter("value", String::class)
                                .returns(launcherClass)
                                .addStatement("password = value")
                                .addStatement("return this")
                                .build())
                        .addType(TypeSpec.companionObjectBuilder()
                                .addFunction(FunSpec.builder("getInstance")
                                        .addParameter("activity", Activity::class)
                                        .addParameter("email", String::class)
                                        .addStatement("return MainActivityLauncher(activity, email)")
                                        .build())
                                .build())
                        .build())
                .build()
    }
}