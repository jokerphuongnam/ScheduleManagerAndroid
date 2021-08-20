package com.pnam.schedulemanager.model.database.network

import com.pnam.schedulemanager.model.database.domain.User
import retrofit2.Response
import java.io.File
import javax.inject.Singleton

@Singleton
interface UsersNetwork {
    suspend fun login(
        email: String? = null,
        password: String? = null,
        userId: String? = null,
        loginId: String? = null
    ): Response<User>

    suspend fun register(
        user: User,
        email: String?,
        password: String?,
        loginId: String?,
        loginType: String?,
        avatar: File?
    ): Response<User>

    suspend fun editProfile(user: User, avatar: File? = null): Response<User>

    suspend fun changeAvatar(userId: String, avatar: File? = null): Response<User>

    suspend fun changePassword(
        userId: String,
        oldPassword: String,
        newPassword: String
    ): Response<Unit>

    suspend fun forgotPassword(email: String): Response<Unit>
}