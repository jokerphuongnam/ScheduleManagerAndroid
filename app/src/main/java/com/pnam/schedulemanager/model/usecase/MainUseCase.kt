package com.pnam.schedulemanager.model.usecase

import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.model.repository.UsersRepository
import javax.inject.Singleton

@Singleton
interface MainUseCase {
    val schedulesRepository: SchedulesRepository
    val usersRepository: UsersRepository

    suspend fun currentUser(): String

    suspend fun getUser(): User
}