package com.pnam.schedulemanager.model.usecase

import android.net.Uri
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.UsersRepository
import javax.inject.Singleton

@Singleton
interface RegisterUseCase {
    val usersRepository: UsersRepository

    suspend fun register(
        user: User,
        email: String?,
        password: String?,
        loginId: String?,
        loginType: String?,
        avatar: Uri? = null
    ): User
}