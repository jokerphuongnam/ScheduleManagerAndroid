package com.pnam.schedulemanager.model.usecase.impl

import android.graphics.Bitmap
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.ScheduleInfoUseCase
import javax.inject.Inject

class DefaultScheduleInfoUseCaseImpl @Inject constructor(
    override val schedulesRepository: SchedulesRepository,
    override val usersRepository: UsersRepository
) : ScheduleInfoUseCase {

    override suspend fun insertSchedule(schedule: Schedule): Schedule {
        return schedulesRepository.insertSchedule(schedule.apply {
            userId = usersRepository.getCurrentUser()
        })
    }

    override suspend fun updateSchedule(schedule: Schedule) {
        schedulesRepository.updateSchedule(schedule.apply {
            userId = usersRepository.getCurrentUser()
        })
    }

    override suspend fun deleteTask(tasksId: String) =
        schedulesRepository.deleteTask(tasksId)

    override suspend fun deleteSchedule(schedule: Schedule) {
        schedulesRepository.deleteSchedule(schedule.apply {
            userId = usersRepository.getCurrentUser()
        })
    }

    override suspend fun getScheduleInfo(scheduleId: String): Schedule =
        schedulesRepository.getScheduleInfo(scheduleId)

    override suspend fun addMultiMedia(scheduleId: String, multiMedia: List<Bitmap>) =
        schedulesRepository.addMultiMedia(
            scheduleId,
            usersRepository.getCurrentUser(),
            multiMedia
        )

    override suspend fun deleteMedia(mediaId: String) = schedulesRepository.deleteMedia(mediaId)

    override suspend fun toggleTask(task: Task, isFinish: Boolean) {
        schedulesRepository.updateTask(task.apply {
            finishBy = if (isFinish) {
                usersRepository.getCurrentUser()
            } else {
                ""
            }
        })
    }
}