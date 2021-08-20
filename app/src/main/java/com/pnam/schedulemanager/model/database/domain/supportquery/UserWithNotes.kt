package com.pnam.schedulemanager.model.database.domain.supportquery

import androidx.room.Embedded
import androidx.room.Relation
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.User

data class UserWithNotes(
    @Embedded val user: User,
    @Relation(
        parentColumn = "schedule_id",
        entityColumn = "schedule_id"
    )
    val schedules: List<Schedule>
)