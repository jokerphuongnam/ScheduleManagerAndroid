package com.pnam.schedulemanager.model.usecase

import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import javax.inject.Singleton

@Singleton
interface SchedulesUseCase {
    val schedulesRepository: SchedulesRepository
    val usersRepository: UsersRepository

    suspend fun getSchedules(): MutableList<Schedule>

    suspend fun deleteTask(vararg tasks: Task): Int

    suspend fun deleteSchedule(schedule: Schedule): Long
}