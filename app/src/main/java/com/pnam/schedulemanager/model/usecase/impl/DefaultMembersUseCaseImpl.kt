package com.pnam.schedulemanager.model.usecase.impl

import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Search
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.model.usecase.MembersUseCase
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DefaultMembersUseCaseImpl @Inject constructor(
    override val usersRepository: UsersRepository,
    override val schedulesRepository: SchedulesRepository
) : MembersUseCase {
    override fun searchUser(searchWord: String, scheduleId: String): Flow<List<Search>> {
        return MutableStateFlow(searchWord).debounce(300).distinctUntilChanged().map {
            usersRepository.searchUser(usersRepository.getCurrentUser(), scheduleId, it, null)
        }
    }

    override suspend fun getSearchResultUser(searchWord: String, scheduleId: String): List<Search> {
        return usersRepository.searchUser(
            usersRepository.getCurrentUser(),
            scheduleId,
            searchWord,
            true
        )
    }

    override suspend fun deleteSearch(searchId: String) {
        usersRepository.deleteSearch(searchId, null)
    }

    override suspend fun clearSearch() {
        usersRepository.deleteSearch(null, usersRepository.getCurrentUser())
    }

    override suspend fun addMember(userIdBeAdded: String, scheduleId: String) {
        schedulesRepository.addMember(userIdBeAdded, scheduleId, usersRepository.getCurrentUser())
    }

    override suspend fun leaveGroup(scheduleId: String) {
        schedulesRepository.leaveGroup(scheduleId, usersRepository.getCurrentUser())
    }

    override suspend fun deleteSchedule(schedule: Schedule) {
        schedulesRepository.deleteSchedule(schedule.apply {
            userId = usersRepository.getCurrentUser()
        })
    }
}