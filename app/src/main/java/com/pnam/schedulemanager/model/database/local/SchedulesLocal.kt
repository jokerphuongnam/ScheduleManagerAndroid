package com.pnam.schedulemanager.model.database.local

import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import javax.inject.Singleton

@Singleton
interface SchedulesLocal {
    suspend fun findSchedules(): MutableList<Schedule>

    suspend fun findLastUpdateSingle(uid: String): Schedule

    suspend fun findSingleSchedule(sid: String): Schedule

    suspend fun findTasksByUid(sid: String): List<Task>

    suspend fun insertSchedulesWithTime(schedules: List<Schedule>)

    suspend fun insertSchedules(schedules: List<Schedule>)

    suspend fun insertTasks(vararg tasks: Task)

    suspend fun updateSchedules(vararg schedules: Schedule): Int

    suspend fun updateTasks(vararg tasks: Task): Int

    suspend fun deleteSchedules(vararg schedules: Schedule)

    suspend fun clearSchedulesByUserId(uid: String)

    suspend fun deleteTasks(vararg tasks: Task)

    suspend fun clearTasksBySchedule(sid: String): Int
}