package com.pnam.schedulemanager.model.usecase

import com.pnam.schedulemanager.model.repository.UsersRepository
import javax.inject.Singleton

@Singleton
interface OptionsAvatarUseCase {
    val userRepository: UsersRepository

    suspend fun deleteAvatar(userId: String)
}