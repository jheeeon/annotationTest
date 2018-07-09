package com.example.naver.annotation

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element

class Field(element: Element) {
    val name: String = element.simpleName.toString()
    val typeName: TypeName = String::class.asTypeName()
}