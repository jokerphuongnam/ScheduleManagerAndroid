package com.pnam.schedulemanager.model.database.domain.supportquery

import androidx.room.Embedded
import androidx.room.Relation
import com.pnam.schedulemanager.model.database.domain.Media
import com.pnam.schedulemanager.model.database.domain.Member
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task

data class ScheduleInfo(
    @Embedded val schedule: Schedule,
    @Relation(
        parentColumn = "schedule_id",
        entityColumn = "schedule_id"
    )
    val tasks: List<Task>,
    @Relation(
        parentColumn = "schedule_id",
        entityColumn = "schedule_id"
    )
    val members: List<Member>,
    @Relation(
        parentColumn = "schedule_id",
        entityColumn = "schedule_id"
    )
    val multiMedia: List<Media>
) {
    fun toSchedule(): Schedule {
        schedule.tasks = tasks
        schedule.members = members
        schedule.images = multiMedia.filter { media -> media.equals(Media.MediaType.IMAGE) }
        schedule.videos = multiMedia.filter { media -> media.equals(Media.MediaType.VIDEO) }
        schedule.audios = multiMedia.filter { media -> media.equals(Media.MediaType.AUDIO) }
        return schedule
    }
}