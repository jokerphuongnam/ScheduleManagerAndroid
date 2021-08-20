package com.pnam.schedulemanager.model.database.local.impl

import com.pnam.schedulemanager.model.database.domain.User
import javax.inject.Singleton

@Singleton
interface UserLocal {
    suspend fun findUsers(): List<User>

    suspend fun findSingleUser(uid: String): User

    suspend fun insertUser(users: User): Long

    suspend fun updateUsers(vararg users: User)

    suspend fun deleteUsers(vararg users: User): Int

    suspend fun deleteUser(uid: Long): Int
}