package com.pnam.schedulemanager.model.usecase.impl

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.ScheduleInfoUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultScheduleInfoUseCaseImpl @Inject constructor(
    override val schedulesRepository: SchedulesRepository,
    override val usersRepository: UsersRepository,
    @ApplicationContext private val context: Context
) : ScheduleInfoUseCase {
    override suspend fun deleteTask(vararg tasks: Task): Int =
        schedulesRepository.deleteTask(*tasks)

    override suspend fun deleteNote(schedule: Schedule): Long =
        usersRepository.currentUser().let { userId ->
            schedule.userId = userId
            schedulesRepository.deleteSchedule(schedule)
        }

    override suspend fun saveNote(
        schedule: Schedule,
        images: List<Uri>,
        sounds: List<Uri>,
        isUpdate: Boolean
    ): Int = usersRepository.currentUser().let { uid ->
        schedule.userId = uid
        val emptyTasks: MutableList<Task> = mutableListOf()
        schedule.tasks.forEach { task ->
            if (task.detail.trim().isEmpty()) {
                emptyTasks.add(task)
            }
        }
        emptyTasks.forEach { emptyTask ->
            schedule.tasks.remove(emptyTask)
        }
        if (isUpdate) {
            schedulesRepository.updateSchedule(
                schedule,
                images.map {
                    it.toFile()
                },
                sounds.map {
                    it.toFile()
                }
            )
        } else {
            schedulesRepository.insertSchedule(
                schedule,
                images.map {
                    it.toFile()
                },
                sounds.map {
                    it.toFile()
                }
            )
        }
    }

    override suspend fun getSchedule(nid: String): Schedule =
        schedulesRepository.getSingleSchedule(nid)
}