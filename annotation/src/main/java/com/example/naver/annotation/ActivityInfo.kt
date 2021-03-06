package com.example.naver.annotation

/**
 * Created by jheeeon on 2018. 7. 4..
 */

class ActivityInfo(var packageName: String, var activityName: String) {
    var requiredFields = ArrayList<Field>()
    var optionalFields = ArrayList<Field>()

    fun addField(field: Field, isRequired: Boolean) {
        if (isRequired) {
            requiredFields.add(field)
        } else {
            optionalFields.add(field)
        }
    }
}