package com.pnam.schedulemanager.model.usecase.impl

import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.SchedulesUseCase
import javax.inject.Inject

class DefaultSchedulesUseCaseImpl @Inject constructor(
    override val schedulesRepository: SchedulesRepository,
    override val usersRepository: UsersRepository
) : SchedulesUseCase {


    override suspend fun getSchedules(): List<Schedule> =
        usersRepository.getCurrentUser().let { uid ->
            schedulesRepository.getSchedules(uid)
        }

    override suspend fun deleteTask(taskId: String) =
        schedulesRepository.deleteTask(taskId)

    override suspend fun deleteSchedule(schedule: Schedule) = usersRepository.getCurrentUser().let { userId ->
        schedule.userId = userId
        schedulesRepository.deleteSchedule(schedule)
    }
}