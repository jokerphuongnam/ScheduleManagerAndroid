package com.pnam.schedulemanager.model.usecase.impl

import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.SettingUseCase
import javax.inject.Inject

class DefaultSettingUseCaseImpl @Inject constructor(
    override val usersRepository: UsersRepository
) : SettingUseCase {
    override suspend fun logout(): Unit = usersRepository.logout()
    override suspend fun getUser(): User = usersRepository.getUser()
}