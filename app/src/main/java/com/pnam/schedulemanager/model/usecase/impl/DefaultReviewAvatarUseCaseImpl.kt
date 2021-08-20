package com.pnam.schedulemanager.model.usecase.impl

import android.net.Uri
import androidx.core.net.toFile
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.ReviewAvatarUseCase
import javax.inject.Inject

class DefaultReviewAvatarUseCaseImpl @Inject constructor(
    override val userRepository: UsersRepository
) : ReviewAvatarUseCase {
    override suspend fun changeAvatar(uid: String, avatar: Uri?): User {
        return userRepository.changeAvatar(uid, avatar?.toFile())
    }
}