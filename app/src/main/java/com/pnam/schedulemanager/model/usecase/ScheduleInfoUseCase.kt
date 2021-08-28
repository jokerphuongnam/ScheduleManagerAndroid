package com.pnam.schedulemanager.model.usecase

import android.graphics.Bitmap
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import javax.inject.Singleton

@Singleton
interface ScheduleInfoUseCase {
    val schedulesRepository: SchedulesRepository
    val usersRepository: UsersRepository

    suspend fun deleteTask(tasksId: String)

    suspend fun insertSchedule(schedule: Schedule): Schedule

    suspend fun updateSchedule(schedule: Schedule)

    suspend fun deleteSchedule(schedule: Schedule)

    suspend fun getScheduleInfo(scheduleId: String): Schedule

    suspend fun addMultiMedia(scheduleId: String, multiMedia: List<Bitmap>)

    suspend fun deleteMedia(mediaId: String)

    suspend fun toggleTask(task: Task, isFinish: Boolean)
}