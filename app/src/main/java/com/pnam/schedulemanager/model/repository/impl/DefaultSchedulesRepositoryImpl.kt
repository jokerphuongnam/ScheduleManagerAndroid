package com.pnam.schedulemanager.model.repository.impl

import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.database.local.SchedulesLocal
import com.pnam.schedulemanager.model.database.network.SchedulesNetwork
import com.pnam.schedulemanager.model.repository.SchedulesRepository
import com.pnam.schedulemanager.throwable.BadRequest
import com.pnam.schedulemanager.throwable.CannotSaveException
import com.pnam.schedulemanager.throwable.NotFoundException
import com.pnam.schedulemanager.utils.RetrofitUtils.BAD_REQUEST
import com.pnam.schedulemanager.utils.RetrofitUtils.INTERNAL_SERVER_ERROR
import com.pnam.schedulemanager.utils.RetrofitUtils.NOT_FOUND
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DefaultSchedulesRepositoryImpl @Inject constructor(
    override val local: SchedulesLocal,
    override val network: SchedulesNetwork
) : SchedulesRepository {

    /**
     * insert to network
     * clear tasks by note
     * insert tasks (list was changed) for note
     * insert note
     * */
    override suspend fun insertSchedule(
        schedule: Schedule,
        images: List<File>,
        sounds: List<File>
    ): Int {
        val response = network.insertSchedule(schedule, images, sounds)
        when {
            response.code() == INTERNAL_SERVER_ERROR -> {
                throw CannotSaveException()
            }
            response.code() == BAD_REQUEST -> {
                throw BadRequest()
            }
            else -> {
                val receiveSchedule: Schedule = response.body()!!
                receiveSchedule.tasks.forEach { task: Task ->
                    task.scheduleId = receiveSchedule.scheduleId
                }
                local.clearTasksBySchedule(receiveSchedule.scheduleId)
                insertTasks(*receiveSchedule.tasks.toTypedArray())
                local.insertSchedules(mutableListOf(receiveSchedule))
                return 0
            }
        }
    }

    override suspend fun insertTasks(vararg tasks: Task): Int = 0

    /**
     * update to network
     * clear task by note
     * insert task (list was changed) for note
     * update note
     * */
    override suspend fun updateSchedule(
        schedule: Schedule,
        images: List<File>,
        sounds: List<File>
    ): Int {
        val response = network.updateSchedule(schedule, images, sounds)
        when {
            response.code() == INTERNAL_SERVER_ERROR -> {
                throw CannotSaveException()
            }
            response.code() == BAD_REQUEST -> {
                throw BadRequest()
            }
            else -> {
                val receiveSchedule: Schedule = response.body()!!
                receiveSchedule.tasks.forEach { task: Task ->
                    task.scheduleId = receiveSchedule.scheduleId
                }
                local.clearTasksBySchedule(receiveSchedule.scheduleId)
                insertTasks(*receiveSchedule.tasks.toTypedArray())
                return local.updateSchedules(receiveSchedule)
            }
        }
    }


    override suspend fun updateTask(vararg tasks: Task): Int = 0

    /**
     * delete note success in network request code != NOT_FOUND (404)
     * delete note in local
     * */
    override suspend fun deleteSchedule(schedule: Schedule): Long {
        val response = network.deleteSchedule(schedule.userId!!, schedule.scheduleId)
        when {
            response.code() == NOT_FOUND -> {
                throw NotFoundException()
            }
            response.code() == BAD_REQUEST -> {
                throw BadRequest()
            }
            else -> {
                local.deleteSchedules(schedule)
                return 1
            }
        }
    }

    override suspend fun deleteTask(vararg tasks: Task): Int {
        local.deleteTasks(*tasks)
        return tasks.size
    }

    override suspend fun clearTasksByNote(nid: String): Int = local.clearTasksBySchedule(nid)

    /**
     * get uid (is always non-null) in data store
     * get count note in api and return Single<Pair<Long, Long>> (uid, count note)
     * configure for Pager
     * convert to flowable
     * */
    override suspend fun getSchedules(uid: String): MutableList<Schedule> {
        val response = network.fetchSchedules(uid)
        when {
            response.code() == NOT_FOUND -> {
                throw NotFoundException()
            }
            response.code() == BAD_REQUEST -> {
                throw BadRequest()
            }
            else -> {
                local.clearSchedulesByUserId(uid)
                val schedules = response.body()!!
                local.insertSchedules(schedules)
                return schedules
            }
        }
    }

    override suspend fun getScheduleInfo(scheduleId: String): Schedule {
        TODO("Not yet implemented")
    }

    override suspend fun getSingleSchedule(uid: String): Schedule =
        local.findSingleSchedule(uid)

    /**
     * clear note of user in cache
     * */
    override suspend fun clearSchedules(uid: String) {
        local.clearSchedulesByUserId(uid)
    }
}