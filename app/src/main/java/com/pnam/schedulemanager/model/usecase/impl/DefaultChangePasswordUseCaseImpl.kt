package com.pnam.schedulemanager.model.usecase.impl

import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.ChangePasswordUseCase
import javax.inject.Inject

class DefaultChangePasswordUseCaseImpl @Inject constructor(override val usersRepository: UsersRepository) :
    ChangePasswordUseCase {
    override suspend fun checkOldPassword(password: String): User =
        usersRepository.getUser().let { user ->
            usersRepository.login(user.userId, password)
        }

    override suspend fun changePassword(oldPassword: String, newPassword: String) =
        usersRepository.getUser().let { user ->
            usersRepository.changePassword(user.userId, oldPassword, newPassword)
        }
}