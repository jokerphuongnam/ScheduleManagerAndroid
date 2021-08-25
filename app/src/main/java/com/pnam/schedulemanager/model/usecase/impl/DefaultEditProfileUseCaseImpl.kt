package com.pnam.schedulemanager.model.usecase.impl

import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.EditProfileUseCase
import javax.inject.Inject

class DefaultEditProfileUseCaseImpl @Inject constructor(
    override val usersRepository: UsersRepository
) : EditProfileUseCase {
    override suspend fun editProfile(user: User) {
        usersRepository.editProfile(user)
    }
}