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


    override suspend fun getSchedules(): MutableList<Schedule> =
        usersRepository.currentUser().let { uid ->
            schedulesRepository.getSchedules(uid)
        }

    override suspend fun deleteTask(vararg tasks: Task): Int =
        schedulesRepository.deleteTask(*tasks)

    /**
     * get id note from user repository
     * set uid for note
     * delete note
     * */
    override suspend fun deleteSchedule(schedule: Schedule): Long = usersRepository.currentUser().let { userId ->
        schedule.userId = userId
        schedulesRepository.deleteSchedule(schedule)
    }
}