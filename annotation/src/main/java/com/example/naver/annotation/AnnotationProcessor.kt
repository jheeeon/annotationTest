package com.example.naver.annotation

import com.google.auto.service.AutoService
import org.jetbrains.annotations.NotNull
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@SupportedAnnotationTypes("com.example.naver.annotation.Launcher", "com.example.naver.annotation.IntentExtra")
@AutoService(Processor::class)
class AnnotationProcessor : AbstractProcessor () {

    private val activityInfoMap: HashMap<String, ActivityInfo> = HashMap()

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        activityInfoMap.clear()

        annotations.forEach { printMessage("annotations $it ${it.simpleName}") }

        roundEnv.getElementsAnnotatedWith(IntentExtra::class.java).forEach { classifyIntentExtraElement(it) }
        roundEnv.getElementsAnnotatedWith(Launcher::class.java).forEach { classifyLauncherElement(it) }

        return writeGeneratedClassToFile()
    }

    private fun classifyIntentExtraElement(annotatedElement: Element) {
        printMessage("Classify Intent Extra Element - ${annotatedElement.simpleName}  ${annotatedElement.asType()}  ${annotatedElement.kind}  ${annotatedElement.enclosingElement}  ${annotatedElement.annotationMirrors}")

        val activity = annotatedElement.enclosingElement
        val activityName = activity.simpleName.toString()
        val activityFullName = activity.toString()
        val packageName = activity.enclosingElement.toString()

        if (!activityInfoMap.containsKey(activityFullName)) {
            activityInfoMap[activityFullName] = ActivityInfo(packageName, activityName)
        }

        val activityInfo = activityInfoMap[activityFullName]
        val isRequired = annotatedElement.getAnnotation(NotNull::class.java) != null
        activityInfo!!.addField(Field(annotatedElement), isRequired)
    }

    private fun classifyLauncherElement(annotatedElement: Element) {
//        printMessage("Classify Launcher Element - ${annotatedElement.simpleName}  ${annotatedElement.asType()}  ${annotatedElement.kind}  ${annotatedElement.enclosingElement}  ${annotatedElement.annotationMirrors}")

        val activityName = annotatedElement.simpleName.toString()
        val activityFullName = annotatedElement.toString()
        val packageName = annotatedElement.enclosingElement.toString()

        if (!activityInfoMap.containsKey(activityFullName)) {
            activityInfoMap[activityFullName] = ActivityInfo(packageName, activityName)
        }
    }

    private fun writeGeneratedClassToFile(): Boolean {
        //build/generated/source/kaptKotlin/{buildType}.{package}에 생성된다
        val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"] ?: run {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find the target directory for generated Kotlin files.")
            return false
        }

        activityInfoMap.values.forEach { ClassGenerator(it).generate(File(kaptKotlinGeneratedDir)) }
        return true
    }

    private fun printMessage(message: CharSequence) {
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, message)
    }
}
