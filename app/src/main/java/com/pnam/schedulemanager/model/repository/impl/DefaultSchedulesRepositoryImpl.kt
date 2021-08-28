package com.pnam.schedulemanager.model.repository.impl

import android.graphics.Bitmap
import com.pnam.schedulemanager.model.database.domain.Media
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.database.local.SchedulesLocal
import com.pnam.schedulemanager.model.database.network.SchedulesNetwork
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.throwable.BadRequest
import com.pnam.schedulemanager.throwable.CannotSaveException
import com.pnam.schedulemanager.throwable.NotFoundException
import com.pnam.schedulemanager.throwable.UnknownException
import com.pnam.schedulemanager.utils.RetrofitUtils.BAD_REQUEST
import com.pnam.schedulemanager.utils.RetrofitUtils.INTERNAL_SERVER_ERROR
import com.pnam.schedulemanager.utils.RetrofitUtils.NOT_FOUND
import com.pnam.schedulemanager.utils.RetrofitUtils.SUCCESS
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DefaultSchedulesRepositoryImpl @Inject constructor(
    override val local: SchedulesLocal,
    override val network: SchedulesNetwork
) : SchedulesRepository {

    override suspend fun insertSchedule(
        schedule: Schedule
    ): Schedule {
        val response = network.insertSchedule(schedule)
        when {
            response.code() == INTERNAL_SERVER_ERROR -> {
                throw CannotSaveException()
            }
            response.code() == BAD_REQUEST -> {
                throw BadRequest()
            }
            else -> {
                val receiveSchedule: Schedule = response.body()!!
                local.insertSchedules(receiveSchedule)
                return receiveSchedule
            }
        }
    }

    override suspend fun updateSchedule(schedule: Schedule) {
        val response = network.updateSchedule(schedule)
        when {
            response.code() == INTERNAL_SERVER_ERROR -> {
                throw CannotSaveException()
            }
            response.code() == BAD_REQUEST -> {
                throw BadRequest()
            }
            else -> {
            }
        }
    }

    override suspend fun deleteSchedule(schedule: Schedule) {
        val response = network.deleteSchedule(schedule.userId!!, schedule.scheduleId)
        when {
            response.code() == NOT_FOUND -> {
                throw NotFoundException()
            }
            response.code() == BAD_REQUEST -> {
                throw BadRequest()
            }
            else -> {
            }
        }
    }

    override suspend fun getSchedules(uid: String): List<Schedule> {
        try {
            val response = network.fetchSchedules(uid)
            when {
                response.code() == NOT_FOUND -> {
                    throw NotFoundException()
                }
                response.code() == BAD_REQUEST -> {
                    throw BadRequest()
                }
                response.code().equals(SUCCESS) -> {
                    local.clearSchedulesByUserId(uid)
                    val schedules = response.body()!!
                    local.insertSchedules(*schedules.toTypedArray())
                    return schedules
                }
                else -> {
                    throw UnknownException()
                }
            }
        } catch (e: Exception) {
            return local.findScheduleByUserId(uid)
        }
    }

    override suspend fun getScheduleInfo(scheduleId: String): Schedule {
        try {
            val response = network.fetchScheduleInfo(scheduleId)
            when {
                response.code() == NOT_FOUND -> {
                    throw NotFoundException()
                }
                response.code() == BAD_REQUEST -> {
                    throw BadRequest()
                }
                response.code().equals(SUCCESS) -> {
                    val receiveSchedule: Schedule = response.body()!!
                    receiveSchedule.members.forEach { member ->
                        member.scheduleId = receiveSchedule.scheduleId
                    }
                    receiveSchedule.tasks.forEach { task ->
                        task.scheduleId = receiveSchedule.scheduleId
                        task.finishBy?.let { memberId ->
                            task.finishByMember = receiveSchedule.members.first { member ->
                                member.memberId == memberId
                            }
                        }
                    }
                    receiveSchedule.images.forEach { image ->
                        image.scheduleId = receiveSchedule.scheduleId
                        image.mediaType = Media.MediaType.IMAGE
                    }
                    receiveSchedule.audios.forEach { audio ->
                        audio.scheduleId = receiveSchedule.scheduleId
                        audio.mediaType = Media.MediaType.AUDIO
                    }
                    receiveSchedule.videos.forEach { video ->
                        video.scheduleId = receiveSchedule.scheduleId
                        video.mediaType = Media.MediaType.AUDIO
                    }
                    // tasks
                    local.clearTasksBySchedule(receiveSchedule.scheduleId)
                    local.insertTasks(*receiveSchedule.tasks.toTypedArray())

                    // members
                    local.clearMemberBySchedule(receiveSchedule.scheduleId)
                    local.insertMembers(*receiveSchedule.members.toTypedArray())

                    // multi media
                    local.clearMultiMediaBySchedule(receiveSchedule.scheduleId)
                    local.insertMultiMedia(*(receiveSchedule.images + receiveSchedule.videos + receiveSchedule.audios).toTypedArray())

                    local.insertSchedules(receiveSchedule)
                    return receiveSchedule
                }
                else -> {
                    throw UnknownException()
                }
            }
        } catch (e: Exception) {
            return local.findSingleSchedule(scheduleId)
        }
    }

    override suspend fun clearSchedules(uid: String) {
        local.clearSchedulesByUserId(uid)
    }

    override suspend fun insertTasks(task: Task, userId: String) {
        val response = network.insertTask(task, userId)
        when {
            response.code() == INTERNAL_SERVER_ERROR -> {
                throw CannotSaveException()
            }
            response.code() == BAD_REQUEST -> {
                throw BadRequest()
            }
            response.code().equals(SUCCESS) -> {
            }
            else -> {
                throw UnknownException()
            }
        }
    }

    override suspend fun updateTask(task: Task) {
        val response = network.updateTask(task)
        when {
            response.code().equals(INTERNAL_SERVER_ERROR) -> {
                throw CannotSaveException()
            }
            response.code().equals(BAD_REQUEST) -> {
                throw BadRequest()
            }
            response.code().equals(SUCCESS) -> {
            }
            else -> {
                throw UnknownException()
            }
        }
    }

    override suspend fun deleteTask(taskId: String) {
        val response = network.deleteTask(taskId)
        when {
            response.code() == INTERNAL_SERVER_ERROR -> {
                throw CannotSaveException()
            }
            response.code() == BAD_REQUEST -> {
                throw BadRequest()
            }
            response.code().equals(SUCCESS) -> {
            }
            else -> {
                throw UnknownException()
            }
        }
    }

    override suspend fun addMember(userIdBeAdded: String, scheduleId: String, userIdAdd: String) {
        val response = network.addMember(userIdBeAdded, scheduleId, userIdAdd)
        when {
            response.code() == INTERNAL_SERVER_ERROR -> {
                throw CannotSaveException()
            }
            response.code() == BAD_REQUEST -> {
                throw BadRequest()
            }
            response.code().equals(SUCCESS) -> {
            }
            else -> {
                throw UnknownException()
            }
        }
    }

    override suspend fun leaveGroup(scheduleId: String, userId: String) {
        val response = network.leaveGroup(scheduleId, userId)
        when {
            response.code() == INTERNAL_SERVER_ERROR -> {
                throw CannotSaveException()
            }
            response.code() == BAD_REQUEST -> {
                throw BadRequest()
            }
            response.code().equals(SUCCESS) -> {
            }
            else -> {
                throw UnknownException()
            }
        }
    }

    override suspend fun addMultiMedia(scheduleId: String, userId: String, multiMedia: List<Bitmap>) {
        val response = network.addMultiMedia(scheduleId, userId, multiMedia)
        when {
            response.code() == INTERNAL_SERVER_ERROR -> {
                throw CannotSaveException()
            }
            response.code() == BAD_REQUEST -> {
                throw BadRequest()
            }
            response.code().equals(SUCCESS) -> {
            }
            else -> {
                throw UnknownException()
            }
        }
    }

    override suspend fun deleteMedia(mediaId: String) {
        val response = network.deleteMedia(mediaId)
        when {
            response.code() == INTERNAL_SERVER_ERROR -> {
                throw CannotSaveException()
            }
            response.code() == BAD_REQUEST -> {
                throw BadRequest()
            }
            response.code().equals(SUCCESS) -> {
            }
            else -> {
                throw UnknownException()
            }
        }
    }

    override suspend fun deleteMultiMedia(multiMediaId: List<String>) {
        val response = network.deleteMultiMedia(multiMediaId)
        when {
            response.code() == INTERNAL_SERVER_ERROR -> {
                throw CannotSaveException()
            }
            response.code() == BAD_REQUEST -> {
                throw BadRequest()
            }
            response.code().equals(SUCCESS) -> {
            }
            else -> {
                throw UnknownException()
            }
        }
    }
}