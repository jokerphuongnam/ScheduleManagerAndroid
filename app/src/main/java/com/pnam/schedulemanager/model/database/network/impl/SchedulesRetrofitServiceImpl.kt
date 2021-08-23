package com.pnam.schedulemanager.model.database.network.impl

import com.pnam.schedulemanager.model.database.domain.Member
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.database.network.SchedulesNetwork
import retrofit2.Response
import retrofit2.http.*
import java.io.File
import javax.inject.Inject


class SchedulesRetrofitServiceImpl @Inject constructor(
    private val service: Service
) : SchedulesNetwork {
    override suspend fun insertSchedule(
        schedule: Schedule
    ): Response<Schedule> {
        return service.createSchedule(
            schedule.title,
            schedule.description,
            schedule.scheduleTime,
            schedule.userId!!,
            schedule.color
        )
    }

    override suspend fun updateSchedule(
        schedule: Schedule
    ): Response<Schedule> {
        return service.updateSchedule(
            schedule.scheduleId,
            schedule.title,
            schedule.description,
            schedule.scheduleTime,
            schedule.userId!!,
            schedule.color
        )
    }

    override suspend fun deleteSchedule(sid: String, nid: String): Response<Schedule> {
        return service.deleteScheduleInfo(sid)
    }

    override suspend fun fetchSchedules(
        uid: Long,
        start: Long,
        amount: Long
    ): Response<MutableList<Schedule>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchScheduleInfo(scheduleId: String): Response<Schedule> {
        return service.fetchScheduleInfo(scheduleId)
    }

    override suspend fun fetchSchedules(uid: String): Response<MutableList<Schedule>> {
        return service.fetchSchedules(uid)
    }

    override suspend fun insertTask(task: Task, userId: String): Response<Unit> {
        return service.insertTask(
            task.detail,
            task.scheduleId,
            userId
        )
    }

    override suspend fun updateTask(task: Task): Response<Unit> {
        return service.updateTask(
            task.taskId,
            task.detail,
            task.finishBy
        )
    }

    override suspend fun deleteTask(taskId: String): Response<Unit> {
        return service.deleteTask(taskId)
    }

    override suspend fun addMember(member: Member): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun leaveGroup(userId: String): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun addMultiMedia(
        scheduleId: String,
        userId: String,
        multiMedia: List<File>
    ): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMedia(mediaId: String): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMultiMedia(multiMediaId: List<String>): Response<Unit> {
        TODO("Not yet implemented")
    }

    interface Service {
        @FormUrlEncoded
        @POST("${SCHEDULES}createschedule")
        suspend fun createSchedule(
            @Field("title") title: String,
            @Field("description") description: String,
            @Field("scheduleTime") scheduleTime: Long,
            @Field("userId") userId: String,
            @Field("color") color: String
        ): Response<Schedule>

        @FormUrlEncoded
        @PUT("${SCHEDULES}editschedule")
        suspend fun updateSchedule(
            @Field("scheduleId") scheduleId: String,
            @Field("title") title: String,
            @Field("description") description: String,
            @Field("scheduleTime") scheduleTime: Long,
            @Field("userId") userId: String,
            @Field("color") color: String
        ): Response<Schedule>

        @GET("${SCHEDULES}delete/{scheduleId}")
        suspend fun deleteScheduleInfo(@Path("scheduleId") scheduleId: String): Response<Schedule>

        @GET("${SCHEDULES}scheduleinfo/{scheduleId}")
        suspend fun fetchScheduleInfo(@Path("scheduleId") scheduleId: String): Response<Schedule>

        @GET("${SCHEDULES}schedules/{uid}")
        suspend fun fetchSchedules(@Path("uid") uid: String): Response<MutableList<Schedule>>

        @FormUrlEncoded
        @POST("${TASKS}createtask")
        suspend fun insertTask(
            @Field("detail") detail: String,
            @Field("scheduleId") scheduleId: String,
            @Field("userId") userId: String
        ): Response<Unit>

        @FormUrlEncoded
        @PUT("${TASKS}edittask")
        suspend fun updateTask(
            @Field("taskId") taskId: String,
            @Field("detail") detail: String?,
            @Field("finishBy") finishBy: String?
        ): Response<Unit>

        @DELETE("${TASKS}deletetask/{taskId}")
        suspend fun deleteTask(@Path("taskId") taskId: String): Response<Unit>

//        suspend fun addMember(): Response<Unit>
//
//        suspend fun leaveGroup(): Response<Unit>
    }

    private companion object {
        private const val SCHEDULES = "schedule/"
        private const val TASKS = "${SCHEDULES}task/"
    }
}