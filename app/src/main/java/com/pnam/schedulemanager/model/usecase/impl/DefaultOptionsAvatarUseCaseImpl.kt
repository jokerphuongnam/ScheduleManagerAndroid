package com.pnam.schedulemanager.model.usecase.impl

import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.OptionsAvatarUseCase
import javax.inject.Inject

class DefaultOptionsAvatarUseCaseImpl @Inject constructor(
    override val userRepository: UsersRepository

) : OptionsAvatarUseCase {
    override suspend fun deleteAvatar(userId: String) {
        userRepository.changeAvatar(userId)
    }
}