package com.pnam.schedulemanager.model.usecase.impl

import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.LoginUseCase
import javax.inject.Inject

class DefaultLoginUseCaseImpl @Inject constructor(
    override val repository: UsersRepository
) : LoginUseCase {
    override suspend fun loginEmailPass(email: String, password: String): User =
        repository.login(email, password)

    override suspend fun loginWithLoginId(loginId: String): User =
        repository.login(null, null, null, loginId)
}