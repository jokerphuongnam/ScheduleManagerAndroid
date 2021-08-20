package com.pnam.schedulemanager.model.database.domain.supportquery

import androidx.room.Embedded
import androidx.room.Relation
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task

data class ScheduleWithTasks(
    @Embedded val schedule: Schedule,
    @Relation(
        parentColumn = "schedule_id",
        entityColumn = "schedule_id"
    )
    val tasks: MutableList<Task>
) {
    /**
     * find note will take a noteWithTask later convert to note
     * - get note by noteWithTask
     * - get tasks and assign for tasks in note
     * */
    fun toNote(): Schedule = schedule.apply {
        tasks = this@ScheduleWithTasks.tasks
    }
}