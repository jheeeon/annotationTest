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
class AnnotationProcessor : AbstractProcessor() {

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
        printMessage("classifyIntentExtraElement - ${annotatedElement.simpleName}  ${annotatedElement.asType()}  ${annotatedElement.kind}  ${annotatedElement.enclosingElement}  ${annotatedElement.annotationMirrors}")

        val activity = annotatedElement.enclosingElement
        val activityFullName = activity.toString()
        val activityName = activity.simpleName.toString()
        val packageName = activity.enclosingElement.toString()

        //Parser 만들 ActivityInfo 추가
        if (!activityInfoMap.containsKey(activityFullName)) {
            activityInfoMap[activityFullName] = ActivityInfo(packageName, activityName)
        }

        //required field, optional field
        val activityInfo = activityInfoMap[activityFullName]
        val isRequired = annotatedElement.getAnnotation(NotNull::class.java) != null
        activityInfo!!.addField(Field(annotatedElement), isRequired)
    }

    private fun classifyLauncherElement(annotatedElement: Element) {
        printMessage("classifyLauncherElement - ${annotatedElement.simpleName}  ${annotatedElement.asType()}  ${annotatedElement.kind}  ${annotatedElement.enclosingElement}  ${annotatedElement.annotationMirrors}")

        val activityName = annotatedElement.simpleName.toString()
        val activityFullName = annotatedElement.toString()
        val packageName = annotatedElement.enclosingElement.toString()

        //Launcher 만들 ActivityInfo 추가
        if (!activityInfoMap.containsKey(activityFullName)) {
            activityInfoMap[activityFullName] = ActivityInfo(packageName, activityName)
        }
    }

    private fun writeGeneratedClassToFile(): Boolean {
        //build/generated/source/kaptKotlin/{buildType}.{package}에 생성된다
        val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"] ?: run {
            printMessage("Can't find the target directory for generated Kotlin files.")
            return false    //writeGeneratedClassToFile 자체가 return false 되는 것. run 자리에 리턴값을 넣고싶으면 return@run
        }

        //activityInfo를 ClassGenerator로 넘겨서 만든 FileSpec을 File로 생성
        activityInfoMap.values.forEach { ClassGenerator(it).generate(File(kaptKotlinGeneratedDir)) }
        return true
    }

    private fun printMessage(message: CharSequence) {
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, message)
    }
}
