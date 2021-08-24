package com.pnam.schedulemanager.model.usecase

import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Search
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface MembersUseCase {
    val usersRepository: UsersRepository
    val schedulesRepository: SchedulesRepository

    fun searchUser(searchWord: String): Flow<List<Search>>

    suspend fun getSearchResultUser(searchWord: String): List<Search>

    suspend fun deleteSearch(searchId: String)

    suspend fun clearSearch()

    suspend fun addMember(userIdBeAdded: String, scheduleId: String)

    suspend fun leaveGroup(scheduleId: String)

    suspend fun deleteSchedule(schedule: Schedule)
}