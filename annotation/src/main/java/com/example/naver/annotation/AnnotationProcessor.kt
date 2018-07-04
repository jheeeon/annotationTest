package com.example.naver.annotation

import android.support.annotation.NonNull
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.asTypeName
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class AnnotationProcessor : AbstractProcessor () {

    private val activityInfoMap: HashMap<String, ActivityInfo> = HashMap()

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Launcher::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(set: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        println("ANNOTATION PROCESS")
        activityInfoMap.clear()
        roundEnv.getElementsAnnotatedWith(Launcher::class.java)
                .forEach { classifyLauncherElement(it) }

        if (activityInfoMap.isEmpty()) {
            processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, "NOTHING TO PROCESS.")
            return false
        }

        val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"] ?: run {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find the target directory for generated Kotlin files.")
            return false
        }

        activityInfoMap.values.forEach { LauncherClassGenerator.generateFileSpec(it).writeTo(File(kaptKotlinGeneratedDir)) }

        return true
    }

    private fun classifyIntentExtraElement(annotatedElement: Element) {
        val activity = annotatedElement.enclosingElement
        val typeMirror = activity.asType()
        val activityName = activity.simpleName.toString()
        val activityFullName = activity.toString()
        val packageName = activity.enclosingElement.toString()

        if (!activityInfoMap.containsKey(activityFullName)) {
            activityInfoMap[activityFullName] = ActivityInfo(packageName, activityName, typeMirror.asTypeName())
        }

        val activityInfo = activityInfoMap[activityFullName]
        val isRequired = annotatedElement.getAnnotation(NonNull::class.java) != null
        activityInfo?.addField(Field(annotatedElement), isRequired)

        //roundsupertypes.....
    }

    private fun classifyLauncherElement(annotatedElement: Element) {
        val typeMirror = annotatedElement.asType()
        val activityName = annotatedElement.simpleName.toString()
        val activityFullName = annotatedElement.toString()
        val packageName = annotatedElement.enclosingElement.toString()

        if (!activityInfoMap.containsKey(activityFullName)) {
            activityInfoMap[activityFullName] = ActivityInfo(packageName, activityName, typeMirror.asTypeName())
        }

        //roundsupertypes.....
    }
}