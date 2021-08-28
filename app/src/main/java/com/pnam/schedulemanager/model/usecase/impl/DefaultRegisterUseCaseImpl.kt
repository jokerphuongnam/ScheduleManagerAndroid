package com.pnam.schedulemanager.model.usecase.impl

import android.graphics.Bitmap
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.RegisterUseCase
import javax.inject.Inject

class DefaultRegisterUseCaseImpl @Inject constructor(
    override val usersRepository: UsersRepository
) : RegisterUseCase {
    override suspend fun register(
        user: User,
        email: String?,
        password: String?,
        loginId: String?,
        loginType: String?,
        avatar: Bitmap?
    ): User =
        usersRepository.register(user, email, password, loginId, loginType, avatar)
}