package com.pnam.schedulemanager.model.usecase.impl

import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.MainUseCase
import javax.inject.Inject

class DefaultMainUseCaseImpl @Inject constructor(
    override val schedulesRepository: SchedulesRepository,
    override val usersRepository: UsersRepository
) : MainUseCase {

    override suspend fun currentUser(): String = usersRepository.currentUser()

    override suspend fun getUser(): User = usersRepository.getUser()
}