package com.pnam.schedulemanager.model.usecase

import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import javax.inject.Singleton

@Singleton
interface SchedulesUseCase {
    val schedulesRepository: SchedulesRepository
    val usersRepository: UsersRepository

    suspend fun getSchedules(): List<Schedule>

    suspend fun deleteTask(taskId: String)

    suspend fun deleteSchedule(schedule: Schedule)
}