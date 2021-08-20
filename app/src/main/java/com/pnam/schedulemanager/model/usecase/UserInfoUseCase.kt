package com.pnam.schedulemanager.model.usecase

import android.net.Uri
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.UsersRepository

interface UserInfoUseCase {
    val usersRepository: UsersRepository

    suspend fun getUser(): User

    suspend fun editProfile(user: User, avatar: Uri? = null): User
}