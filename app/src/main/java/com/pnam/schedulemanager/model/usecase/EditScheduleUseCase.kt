package com.pnam.schedulemanager.model.usecase

import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import javax.inject.Singleton

@Singleton
interface EditScheduleUseCase {
    val schedulesRepository: SchedulesRepository
    val usersRepository: UsersRepository
}