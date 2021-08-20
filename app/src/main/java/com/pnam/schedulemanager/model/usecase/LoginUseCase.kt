package com.pnam.schedulemanager.model.usecase

import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.UsersRepository
import javax.inject.Singleton

@Singleton
interface LoginUseCase {
    val repository: UsersRepository

    suspend fun loginEmailPass(email: String, password: String): User

    suspend fun loginWithLoginId(loginId: String): User
}