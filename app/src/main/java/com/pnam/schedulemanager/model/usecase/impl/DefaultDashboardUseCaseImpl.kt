package com.pnam.schedulemanager.model.usecase.impl

import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.DashboardUseCase
import com.pnam.schedulemanager.throwable.NotFoundException
import com.pnam.schedulemanager.throwable.NotLoginException
import javax.inject.Inject

class DefaultDashboardUseCaseImpl @Inject constructor(
    override val usersRepository: UsersRepository,
    override val schedulesRepository: SchedulesRepository
) : DashboardUseCase {

    override suspend fun getUser(): User {
        try {
            return usersRepository.login(null, null, usersRepository.getCurrentUser(), null)
        } catch (e : NotFoundException) {
            throw NotLoginException()
        }
    }

    override suspend fun getSchedules(): List<Schedule> {
       return schedulesRepository.getSchedules(usersRepository.getCurrentUser())
    }

    override suspend fun deleteTask(tasksId: String) {
        return schedulesRepository.deleteTask(tasksId)
    }
}