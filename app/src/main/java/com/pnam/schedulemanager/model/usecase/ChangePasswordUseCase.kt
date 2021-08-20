package com.pnam.schedulemanager.model.usecase

import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.UsersRepository
import javax.inject.Singleton

@Singleton
interface ChangePasswordUseCase {
    val usersRepository: UsersRepository

    suspend fun checkOldPassword(password: String): User

    suspend fun changePassword(oldPassword: String, newPassword: String)
}