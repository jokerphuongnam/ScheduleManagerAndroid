package com.pnam.schedulemanager.model.database.local

import javax.inject.Singleton

@Singleton
interface LocalReference {
    suspend fun isDarkMode(): Boolean
    fun changeTheme(boolean: Boolean): Long
}