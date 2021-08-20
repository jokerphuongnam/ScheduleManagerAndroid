package com.pnam.schedulemanager.model.database.local.impl

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.pnam.schedulemanager.model.database.domain.User

@Dao
interface RoomUserImpl : UserLocal {
    @Query("SELECT * FROM USERS")
    override suspend fun findUsers(): List<User>

    @Query("SELECT * FROM USERS WHERE user_id = :uid")
    override suspend fun findSingleUser(uid: String): User

    @Insert(onConflict = REPLACE)
    override suspend fun insertUser(users: User): Long

    @Update
    override suspend fun updateUsers(vararg users: User)

    @Delete
    override suspend fun deleteUsers(vararg users: User): Int

    @Query("DELETE FROM USERS WHERE user_id = :uid")
    override suspend fun deleteUser(uid: Long): Int
}