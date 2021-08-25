package com.pnam.schedulemanager.model.usecase

import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.UsersRepository
import javax.inject.Singleton

@Singleton
interface EditProfileUseCase {
    val usersRepository: UsersRepository

    suspend fun editProfile(user: User)
}