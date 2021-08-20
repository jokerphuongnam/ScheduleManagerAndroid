package com.pnam.schedulemanager.model.usecase

import com.pnam.schedulemanager.model.repository.UsersRepository
import javax.inject.Singleton

@Singleton
interface ForgotPasswordUseCase {
    val usersRepository: UsersRepository

    suspend fun forgotPassword(userId: String)
}