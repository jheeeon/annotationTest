package com.example.naver.annotation

import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import org.apache.commons.lang3.StringUtils
import javax.lang.model.type.TypeMirror
import javax.lang.model.element.Element

/**
 * Created by jheeeon on 2018. 7. 4..
 */

class Field(element: Element) {
    val name: String = element.simpleName.toString().removeSuffix("\$annotations")
    val typeName: TypeName = String::class.asTypeName()

//    val typeMirror: TypeMirror = element.asType()
//    val typeName: TypeName = typeMirror.asTypeName()
//    val typeArguments: List<TypeName>? = if (typeName is ParameterizedTypeName) typeName.typeArguments else null
}