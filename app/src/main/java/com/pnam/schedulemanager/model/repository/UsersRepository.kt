package com.pnam.schedulemanager.model.repository

import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.database.local.CurrentUser
import com.pnam.schedulemanager.model.database.local.impl.UserLocal
import com.pnam.schedulemanager.model.database.network.UsersNetwork
import java.io.File
import javax.inject.Singleton

@Singleton
interface UsersRepository {
    val local: UserLocal
    val network: UsersNetwork
    val currentUser: CurrentUser

    suspend fun getCurrentUser(): String

    suspend fun login(
        email: String? = null,
        password: String? = null,
        userId: String? = null,
        loginId: String? = null
    ): User

    suspend fun logout()

    suspend fun deleteUser(uid: Long): Int

    suspend fun editProfile(user: User, avatar: File?): User

    suspend fun changeAvatar(userId: String, avatar: File? = null): User

    suspend fun changePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    )

    suspend fun forgotPassword(userId: String)

    suspend fun register(
        user: User,
        email: String?,
        password: String?,
        loginId: String?,
        loginType: String?,
        avatar: File?
    ): User

    suspend fun getUser(): User
}