package com.pnam.schedulemanager.model.usecase.impl

import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.EditScheduleUseCase
import javax.inject.Inject

class DefaultEditScheduleUseCaseImpl @Inject constructor(
    override val schedulesRepository: SchedulesRepository,
    override val usersRepository: UsersRepository
) : EditScheduleUseCase