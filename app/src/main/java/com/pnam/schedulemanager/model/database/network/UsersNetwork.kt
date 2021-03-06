package com.pnam.schedulemanager.model.database.network

import android.graphics.Bitmap
import com.pnam.schedulemanager.model.database.domain.Search
import com.pnam.schedulemanager.model.database.domain.User
import retrofit2.Response
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
        avatar: Bitmap?
    ): Response<User>

    suspend fun editProfile(user: User): Response<User>

    suspend fun changeAvatar(userId: String, avatar: Bitmap? = null): Response<User>

    suspend fun deleteAvatar(userId: String): Response<User>

    suspend fun changePassword(
        userId: String,
        oldPassword: String,
        newPassword: String
    ): Response<Unit>

    suspend fun forgotPassword(email: String): Response<Unit>

    suspend fun searchUser(
        userId: String,
        scheduleId: String,
        searchWord: String,
        isInsert: Boolean? = null
    ): Response<List<Search>>

    suspend fun deleteSearch(
        searchId: String?,
        userId: String?
    ): Response<Unit>
}