package com.example.naver.annotation

import android.app.Activity
import android.content.Intent
import com.squareup.kotlinpoet.*
import java.io.File

const val LAUNCHER_SUFFIX = "Launcher"
const val PARSER_SUFFIX = "Parser"

class ClassGenerator(activityInfo: ActivityInfo) {

    private val packageName = activityInfo.packageName
    private val activityName = activityInfo.activityName
    private val launcherClassName = activityName.plus(LAUNCHER_SUFFIX)
    private val parserClassName = activityName.plus(PARSER_SUFFIX)
    private val activityClass = ClassName(packageName, activityName)
    private val launcherClass = ClassName(packageName, launcherClassName)
    private val requiredFields = activityInfo.requiredFields
    private val optionalFields = activityInfo.optionalFields

    fun generate(writeTo: File) {
        generateLauncherFileSpec().writeTo(writeTo)
        generateParserFileSpec().writeTo(writeTo)
    }

    private fun generateLauncherFileSpec(): FileSpec {
        return FileSpec.builder(packageName, launcherClassName)
                .addType(TypeSpec.classBuilder(launcherClassName)
                        .primaryConstructor(FunSpec.constructorBuilder()
                                .addParameter("activity", Activity::class)
                                .addRequiredParameters()
                                .build())
                        .addProperty(PropertySpec
                                .varBuilder("activity", Activity::class)
                                .initializer("activity")
                                .build())
                        .addProperty(PropertySpec
                                .builder("intent", Intent::class)
                                .initializer("Intent(activity, $activityName::class.java)${putRequiredExtras()}")
                                .build())
                        .addRequiredProperties()
                        .addOptionalPropertiesAndSetter()
                        .addFunction(FunSpec
                                .builder("startActivity")
                                .addStatement("intent.putExtra(\"activityParser\", $parserClassName::class.java)")
                                .addStatement("activity.startActivity(intent)")
                                .build())
                        .addType(TypeSpec
                                .companionObjectBuilder()
                                .addFunction(FunSpec
                                        .builder("getInstance")
                                        .addParameter("activity", Activity::class)
                                        .addRequiredParameters()
                                        .addStatement("return $launcherClassName(${requiredParams()})")
                                        .build())
                                .build())
                        .build())
                .build()
    }

    private fun generateParserFileSpec(): FileSpec {
        return FileSpec.builder(packageName, parserClassName)
                .addType(TypeSpec.classBuilder(parserClassName)
                        .primaryConstructor(FunSpec.constructorBuilder()
                                .addParameter("activity", activityClass)
                                .build())
                        .addProperty(PropertySpec.varBuilder("activity", activityClass).initializer("activity").build())
                        .addProperty(PropertySpec.builder("intent", Intent::class)
                                .initializer("activity.intent")
                                .build())
                        .addGetters()
                        .addFunction(FunSpec
                                .builder("parseAll")
                                .addParsingStatements()
                                .build())
                        .build())
                .build()

    }

    private fun FunSpec.Builder.addRequiredParameters(): FunSpec.Builder {
        requiredFields.forEach { this.addParameter(it.name, it.typeName) }
        return this
    }

    private fun FunSpec.Builder.addParsingStatements(): FunSpec.Builder {
        requiredFields.forEach {
            this.addStatement("activity.${it.name} = get${it.name}()")
        }

        optionalFields.forEach {
            this.addStatement("activity.${it.name} = get${it.name}()")
        }
        return this
    }

    private fun TypeSpec.Builder.addRequiredProperties(): TypeSpec.Builder {
        requiredFields.forEach {
            this.addProperty(PropertySpec
                    .builder(it.name, it.typeName)
                    .initializer(it.name)
                    .build())
        }
        return this
    }

    private fun TypeSpec.Builder.addOptionalPropertiesAndSetter(): TypeSpec.Builder {
        optionalFields.forEach {
            this.addProperty(PropertySpec
                    .varBuilder(it.name, it.typeName)
                    .initializer("%S", "")
                    .setter(FunSpec.setterBuilder()
                            .addParameter("value", it.typeName)
                            .addStatement("field = value")
                            .addStatement("intent.putExtra(\"${it.name}\", value)")
                            .build())
                    .build())
                    .addFunction(FunSpec
                            .builder(it.name)
                            .addParameter("value", it.typeName)
                            .returns(launcherClass)
                            .addStatement("${it.name} = value")
                            .addStatement("return this")
                            .build())
        }
        return this
    }

    private fun TypeSpec.Builder.addGetters(): TypeSpec.Builder {
        requiredFields.forEach {
            this.addFunction(FunSpec
                    .builder("get${it.name}")
                    .returns(it.typeName.asNonNullable())
                    .addStatement("return intent.getStringExtra(\"${it.name}\")")
                    .build())
        }
        optionalFields.forEach {
            this.addFunction(FunSpec
                    .builder("get${it.name}")
                    .returns(it.typeName.asNullable())
                    .addStatement("return intent.getStringExtra(\"${it.name}\") ?: null")
                    .build())
        }
        return this
    }

    private fun putRequiredExtras(): String {
        var str = ""
        requiredFields.forEach { str += ".putExtra(\"${it.name}\", ${it.name})" }
        return str
    }

    private fun requiredParams(): String {
        var param = "activity"
        requiredFields.forEach { param += ", ${it.name}" }
        return param
    }
}
