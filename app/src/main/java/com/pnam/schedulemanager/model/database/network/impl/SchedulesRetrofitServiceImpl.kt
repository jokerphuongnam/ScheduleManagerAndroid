package com.pnam.schedulemanager.model.database.network.impl

import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.network.SchedulesNetwork
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.File
import javax.inject.Inject


class SchedulesRetrofitServiceImpl @Inject constructor(
    private val service: Service
) : SchedulesNetwork {
    override suspend fun insertSchedule(
        schedule: Schedule,
        images: List<File>,
        sounds: List<File>
    ): Response<Schedule> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSchedule(
        schedule: Schedule,
        images: List<File>,
        sounds: List<File>
    ): Response<Schedule> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSchedule(sid: String, nid: String): Response<Schedule> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchSchedules(
        uid: Long,
        start: Long,
        amount: Long
    ): Response<MutableList<Schedule>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchSchedules(uid: String): Response<MutableList<Schedule>> {
        return service.fetchSchedules(uid)
    }

    interface Service {
        @GET("${PATH}schedules/{uid}")
        suspend fun fetchSchedules(@Path("uid") uid: String): Response<MutableList<Schedule>>
    }

    private companion object {
        private const val PATH = "schedule/"
    }
}