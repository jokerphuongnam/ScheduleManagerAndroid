package com.pnam.schedulemanager.model.usecase

import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import javax.inject.Singleton

@Singleton
interface InputTaskUseCase {
    val usersRepository: UsersRepository
    val schedulesRepository: SchedulesRepository

    suspend fun insertTask(task: Task)

    suspend fun updateTask(task: Task)
}