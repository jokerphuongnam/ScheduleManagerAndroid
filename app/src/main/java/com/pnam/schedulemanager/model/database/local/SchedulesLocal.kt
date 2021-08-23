package com.pnam.schedulemanager.model.database.local

import com.pnam.schedulemanager.model.database.domain.Media
import com.pnam.schedulemanager.model.database.domain.Member
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import javax.inject.Singleton

@Singleton
interface SchedulesLocal {
    suspend fun findSchedules(): List<Schedule>

    suspend fun findLastUpdateSingle(uid: String): Schedule

    suspend fun findScheduleByUserId(userId: String): List<Schedule>

    suspend fun findSingleSchedule(sid: String): Schedule

    suspend fun findTasksByScheduleId(sid: String): List<Task>

    suspend fun insertSchedulesWithTime(vararg schedules: Schedule)

    suspend fun insertSchedules(vararg schedules: Schedule)

    suspend fun updateSchedules(vararg schedules: Schedule)

    suspend fun deleteSchedules(vararg schedules: Schedule)

    suspend fun clearSchedulesByUserId(uid: String)

    suspend fun insertTasks(vararg tasks: Task)

    suspend fun updateTasks(vararg tasks: Task)

    suspend fun deleteTasks(vararg tasks: Task)

    suspend fun clearTasksBySchedule(sid: String)

    suspend fun insertMembers(vararg member: Member)

    suspend fun updateMembers(vararg member: Member)

    suspend fun deleteMembers(vararg member: Member)

    suspend fun clearMemberBySchedule(sid: String)

    suspend fun insertMultiMedia(vararg media: Media)

    suspend fun updateMultiMedia(vararg media: Media)

    suspend fun deleteMultiMedia(vararg media: Media)

    suspend fun clearMultiMediaBySchedule(sid: String)
}