package com.pnam.schedulemanager.utils

fun List<String>?.toJson(): String {
    return if (this == null || isEmpty()) {
        "[]"
    } else {
        var json = "["
        forEach {
            json += "\"${it}\","
        }
        json = json.substring(0, json.length - 1)
        json = "${json}]"
        json
    }
}