package com.pnam.schedulemanager.model.usecase.impl

import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.InputTaskUseCase
import javax.inject.Inject

class DefaultInputTaskUseCaseImpl @Inject constructor(
    override val usersRepository: UsersRepository,
    override val schedulesRepository: SchedulesRepository
) : InputTaskUseCase {
    override suspend fun insertTask(task: Task) {
        schedulesRepository.insertTasks(task, usersRepository.getCurrentUser())
    }

    override suspend fun updateTask(task: Task) {
        schedulesRepository.updateTask(task)
    }
}