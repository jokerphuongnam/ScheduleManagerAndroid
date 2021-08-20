package com.pnam.schedulemanager.model.database.local

import javax.inject.Singleton

@Singleton
interface CurrentUser {
    suspend fun findUid(): String?
    suspend fun changeCurrentUser(uid: String)
    suspend fun signOut()
}