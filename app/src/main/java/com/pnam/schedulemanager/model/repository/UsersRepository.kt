package com.pnam.schedulemanager.model.repository

import android.graphics.Bitmap
import com.pnam.schedulemanager.model.database.domain.Search
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.database.local.CurrentUser
import com.pnam.schedulemanager.model.database.local.impl.UserLocal
import com.pnam.schedulemanager.model.database.network.UsersNetwork
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

    suspend fun editProfile(user: User): User

    suspend fun changeAvatar(userId: String, avatar: Bitmap? = null): User

    suspend fun deleteAvatar(userId: String): User

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
        avatar: Bitmap?
    ): User

    suspend fun getUser(): User

    suspend fun searchUser(
        userId: String,
        scheduleId: String,
        searchWord: String,
        isInsert: Boolean? = null
    ): List<Search>

    suspend fun deleteSearch(
        searchId: String?,
        userId: String?
    )
}