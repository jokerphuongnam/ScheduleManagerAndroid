package com.pnam.schedulemanager.model.usecase

import android.net.Uri
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import javax.inject.Singleton

@Singleton
interface ScheduleInfoUseCase {
    val schedulesRepository: SchedulesRepository
    val usersRepository: UsersRepository

    suspend fun deleteTask(
        vararg tasks: Task
    ): Int

    suspend fun deleteNote(schedule: Schedule): Long

    suspend fun saveNote(
        schedule: Schedule,
        images: List<Uri>,
        sounds: List<Uri>,
        isUpdate: Boolean = false
    ): Int

    suspend fun getSchedule(nid: String): Schedule
}