package com.pnam.schedulemanager.model.repository

import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.database.local.SchedulesLocal
import com.pnam.schedulemanager.model.database.network.SchedulesNetwork
import java.io.File
import javax.inject.Singleton

@Singleton
interface SchedulesRepository {
    val local: SchedulesLocal
    val network: SchedulesNetwork

    suspend fun insertSchedule(
        schedule: Schedule,
        images: List<File>,
        sounds: List<File>
    ): Int

    suspend fun insertTasks(
        vararg tasks: Task
    ): Int

    suspend fun updateSchedule(
        schedule: Schedule,
        images: List<File>,
        sounds: List<File>
    ): Int

    suspend fun updateTask(
        vararg tasks: Task
    ): Int

    suspend fun deleteSchedule(schedule: Schedule): Long

    suspend fun deleteTask(
        vararg tasks: Task
    ): Int

    suspend fun clearTasksByNote(
        nid: String
    ): Int

    suspend fun getSchedules(uid: String): MutableList<Schedule>

    suspend fun getScheduleInfo(scheduleId: String): Schedule

    suspend fun getSingleSchedule(uid: String): Schedule

    suspend fun clearSchedules(uid: String)
}