package com.pnam.schedulemanager.utils

import com.pnam.schedulemanager.model.database.domain.Task
import com.google.gson.*
import java.lang.reflect.Type
//
//class TaskGsonAdapter : JsonSerializer<Task> {
//    override fun serialize(
//        src: Task,
//        typeOfSrc: Type?,
//        context: JsonSerializationContext?
//    ): JsonElement {
//        val json = JsonObject()
//        json.addProperty(Task::is.name, src.isFinish)
//        json.addProperty(Task::detail.name, src.detail)
//        return json
//    }
//}