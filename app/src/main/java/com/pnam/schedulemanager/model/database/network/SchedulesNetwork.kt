package com.pnam.schedulemanager.model.database.network

import com.pnam.schedulemanager.model.database.domain.Schedule
import retrofit2.Response
import java.io.File
import javax.inject.Singleton

@Singleton
interface SchedulesNetwork {
    suspend fun insertSchedule(
        schedule: Schedule,
        images: List<File>,
        sounds: List<File>
    ): Response<Schedule>

    suspend fun updateSchedule(
        schedule: Schedule,
        images: List<File>,
        sounds: List<File>
    ): Response<Schedule>

    suspend fun deleteSchedule(
        sid: String,
        nid: String
    ): Response<Schedule>

    suspend fun fetchSchedules(uid: Long, start: Long, amount: Long): Response<MutableList<Schedule>>
    suspend fun fetchSchedules(uid: String): Response<MutableList<Schedule>>
}