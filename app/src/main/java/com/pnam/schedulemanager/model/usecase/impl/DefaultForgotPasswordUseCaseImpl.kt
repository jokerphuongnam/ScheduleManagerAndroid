package com.pnam.schedulemanager.model.usecase.impl

import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.ForgotPasswordUseCase
import javax.inject.Inject

class DefaultForgotPasswordUseCaseImpl @Inject constructor(
    override val usersRepository: UsersRepository
) : ForgotPasswordUseCase {
    override suspend fun forgotPassword(userId: String) = usersRepository.forgotPassword(userId)
}