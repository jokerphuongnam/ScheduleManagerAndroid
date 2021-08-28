package com.pnam.schedulemanager.model.repository

import android.graphics.Bitmap
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

    suspend fun insertSchedule(schedule: Schedule): Schedule

    suspend fun updateSchedule(schedule: Schedule)

    suspend fun deleteSchedule(schedule: Schedule)

    suspend fun getSchedules(uid: String): List<Schedule>

    suspend fun getScheduleInfo(scheduleId: String): Schedule

    suspend fun clearSchedules(uid: String)

    suspend fun insertTasks(task: Task, userId: String)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(taskId: String)

    suspend fun addMember(userIdBeAdded: String, scheduleId: String, userIdAdd: String)

    suspend fun leaveGroup(scheduleId: String, userId: String)

    suspend fun addMultiMedia(scheduleId: String, userId: String, multiMedia: List<Bitmap>)

    suspend fun deleteMedia(mediaId: String)

    suspend fun deleteMultiMedia(multiMediaId: List<String>)
}