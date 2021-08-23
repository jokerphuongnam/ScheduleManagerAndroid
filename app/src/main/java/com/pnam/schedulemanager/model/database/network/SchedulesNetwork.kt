package com.pnam.schedulemanager.model.database.network

import com.pnam.schedulemanager.model.database.domain.Member
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import retrofit2.Response
import java.io.File
import javax.inject.Singleton

@Singleton
interface SchedulesNetwork {
    suspend fun insertSchedule(
        schedule: Schedule,
    ): Response<Schedule>

    suspend fun updateSchedule(
        schedule: Schedule
    ): Response<Schedule>

    suspend fun deleteSchedule(
        sid: String,
        nid: String
    ): Response<Schedule>

    suspend fun fetchSchedules(
        uid: Long,
        start: Long,
        amount: Long
    ): Response<MutableList<Schedule>>

    suspend fun fetchSchedules(uid: String): Response<MutableList<Schedule>>

    suspend fun fetchScheduleInfo(scheduleId: String): Response<Schedule>

    suspend fun insertTask(task: Task, userId: String): Response<Unit>

    suspend fun updateTask(task: Task): Response<Unit>

    suspend fun deleteTask(taskId: String): Response<Unit>

    suspend fun addMember(member: Member): Response<Unit>

    suspend fun leaveGroup(userId: String): Response<Unit>

    suspend fun addMultiMedia(scheduleId: String, userId: String, multiMedia: List<File>): Response<Unit>

    suspend fun deleteMedia(mediaId: String): Response<Unit>

    suspend fun deleteMultiMedia(multiMediaId: List<String>): Response<Unit>
}