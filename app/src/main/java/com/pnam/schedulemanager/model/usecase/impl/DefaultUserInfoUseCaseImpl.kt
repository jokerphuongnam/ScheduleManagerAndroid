package com.pnam.schedulemanager.model.usecase.impl

import android.net.Uri
import androidx.core.net.toFile
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.UserInfoUseCase
import javax.inject.Inject

class DefaultUserInfoUseCaseImpl @Inject constructor(
    override val usersRepository: UsersRepository
) : UserInfoUseCase {
    override suspend fun getUser(): User = usersRepository.getUser()

    override suspend fun editProfile(user: User, avatar: Uri?): User =
        usersRepository.editProfile(user, avatar?.toFile())
}