package com.pnam.schedulemanager.model.repository.impl

import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.model.database.local.CurrentUser
import com.pnam.schedulemanager.model.database.local.impl.UserLocal
import com.pnam.schedulemanager.model.database.network.UsersNetwork
import com.pnam.schedulemanager.model.repository.UsersRepository
import com.pnam.schedulemanager.throwable.*
import com.pnam.schedulemanager.utils.RetrofitUtils.BAD_REQUEST
import com.pnam.schedulemanager.utils.RetrofitUtils.CONFLICT
import com.pnam.schedulemanager.utils.RetrofitUtils.NOT_FOUND
import com.pnam.schedulemanager.utils.RetrofitUtils.SUCCESS
import java.io.File
import javax.inject.Inject

class DefaultUsersRepositoryImpl @Inject constructor(
    override val local: UserLocal,
    override val network: UsersNetwork,
    override val currentUser: CurrentUser
) : UsersRepository {
    /**
     * get current user single flowable first time or first time is complete will return error
     * */
    override suspend fun currentUser(): String = currentUser.findUid() ?: throw NotFoundException()

    /**
     * when send login will receive 1 repose body if request code == 404 wrong username or password
     * add username for user and save for currentUser (data store) and room (save cache)
     * */
    override suspend fun login(
        email: String?,
        password: String?,
        userId: String?,
        loginId: String?
    ): User {
        val response = network.login(email, password, userId, loginId)
        if (response.code().equals(NOT_FOUND)) {
            throw NotFoundException()
        } else if (response.code().equals(SUCCESS)) {
            val user = response.body()!!
            local.insertUser(user)
            currentUser.changeCurrentUser(user.userId)
            return user
        } else {
            throw UnknownException()
        }
    }

    override suspend fun logout() = currentUser.signOut()

    override suspend fun deleteUser(uid: Long): Int = local.deleteUser(uid)

    override suspend fun editProfile(user: User, avatar: File?): User {
        return network.editProfile(user, avatar).let { response ->
            val responseCode = response.code()
            when {
                responseCode.equals(CONFLICT) -> {
                    throw WrongException()
                }
                responseCode.equals(SUCCESS) -> {
                    val user = response.body()!!
                    local.updateUsers(user)
                    user
                }
                else -> {
                    throw UnknownException()
                }
            }
        }
    }

    override suspend fun changeAvatar(userId: String, avatar: File?): User {
        val response = network.changeAvatar(userId, avatar)
        val responseCode = response.code()
        when {
            responseCode.equals(SUCCESS) -> {
                val user = response.body()!!
                local.updateUsers(user)
                return user
            }
            responseCode.equals(NOT_FOUND) -> {
                throw NotFoundException()
            }
            else -> {
                throw UnknownException()
            }
        }
    }

    override suspend fun changePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ) = network.changePassword(email, oldPassword, newPassword).let { response ->
        val responseCode = response.code()
        when {
            responseCode.equals(NOT_FOUND) -> {
                throw NotFoundException()
            }
            responseCode.equals(SUCCESS) -> {
                response.body()!!
            }
            else -> {
                throw UnknownException()
            }
        }
    }

    override suspend fun forgotPassword(userId: String) {
        val responseCode = network.forgotPassword(userId).code()
        if (responseCode.equals(NOT_FOUND)) {
            throw  NotFoundException()
        } else {
            throw UnknownException()
        }
    }

    override suspend fun register(
        user: User,
        email: String?,
        password: String?,
        loginId: String?,
        loginType: String?,
        avatar: File?
    ): User {
        val response = network.register(user, email, password, loginId, loginType, avatar)
        val responseCode = response.code()
        when {
            responseCode.equals(CONFLICT) -> {
                throw  WrongException()
            }
            responseCode.equals(BAD_REQUEST) -> {
                throw  BadRequest()
            }
            responseCode.equals(SUCCESS) -> {
                return response.body()!!
            }
            else -> {
                throw UnknownException()
            }
        }
    }

    override suspend fun getUser(): User = currentUser.findUid().let {
        try {
            val response = network.login(null, null, it, null)
            val responseCode = response.code()
            when {
                responseCode.equals(SUCCESS) -> {
                    val user = response.body()!!
                    local.insertUser(user)
                    return@let user
                }
                responseCode.equals(NOT_FOUND) -> {
                    throw NotFoundException()
                }
                else -> {
                    throw UnknownException()
                }
            }
        } catch (e: Exception) {
            return@let local.findSingleUser(it!!)
        }
    }
}