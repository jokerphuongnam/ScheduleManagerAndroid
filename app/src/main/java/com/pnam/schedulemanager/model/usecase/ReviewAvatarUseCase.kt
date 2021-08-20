package com.pnam.schedulemanager.model.usecase

import android.net.Uri
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.UsersRepository
import javax.inject.Singleton

@Singleton
interface ReviewAvatarUseCase {
    val userRepository: UsersRepository

    suspend fun changeAvatar(uid: String, avatar: Uri? = null): User
}