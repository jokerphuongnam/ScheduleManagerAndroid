package com.pnam.schedulemanager.model.database.local.impl

import androidx.room.*
import com.pnam.schedulemanager.model.database.domain.Media
import com.pnam.schedulemanager.model.database.domain.Member
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.database.domain.supportquery.ScheduleInfo
import com.pnam.schedulemanager.model.database.local.SchedulesLocal

@Dao
interface RoomSchedulesImpl : SchedulesLocal {
    @Query("SELECT schedule_id, title, description, created_at, color, modified_at, schedule_time, user_id FROM SCHEDULES ORDER BY schedule_id")
    override suspend fun findSchedules(): List<Schedule>

    @Query("SELECT  schedule_id, title, description, created_at, color, modified_at, schedule_time, user_id FROM SCHEDULES WHERE user_id = :uid ORDER BY created_at DESC LIMIT 1")
    override suspend fun findLastUpdateSingle(uid: String): Schedule

    @Query("SELECT * FROM SCHEDULES WHERE user_id = :userId")
    override suspend fun findScheduleByUserId(userId: String): List<Schedule>

    @Query("SELECT * FROM SCHEDULES WHERE schedule_id = :sid")
    suspend fun findSchedulesInfo(sid: String): ScheduleInfo

    override suspend fun findSingleSchedule(sid: String): Schedule =
        findSchedulesInfo(sid).toSchedule()

    @Query("SELECT * FROM TASKS WHERE schedule_id = :sid")
    override suspend fun findTasksByScheduleId(sid: String): List<Task>

    override suspend fun insertSchedulesWithTime(vararg schedules: Schedule) {
        schedules.map { note ->
            note.apply {
                createAt = System.currentTimeMillis()
                modifiedAt = System.currentTimeMillis()
            }
        }
        insertSchedules(*schedules)
    }

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertSchedules(vararg schedules: Schedule)

    @Transaction
    @Update
    override suspend fun updateSchedules(vararg schedules: Schedule)

    @Transaction
    @Delete
    override suspend fun deleteSchedules(vararg schedules: Schedule)

    @Transaction
    @Query("DELETE FROM SCHEDULES WHERE user_id = :uid")
    override suspend fun clearSchedulesByUserId(uid: String)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertTasks(vararg tasks: Task)

    @Transaction
    @Update
    override suspend fun updateTasks(vararg tasks: Task)

    @Transaction
    @Delete
    override suspend fun deleteTasks(vararg tasks: Task)

    @Transaction
    @Query("DELETE FROM TASKS WHERE schedule_id = :sid")
    override suspend fun clearTasksBySchedule(sid: String)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertMembers(vararg member: Member)

    @Transaction
    @Update
    override suspend fun updateMembers(vararg member: Member)

    @Transaction
    @Delete
    override suspend fun deleteMembers(vararg member: Member)

    @Transaction
    @Query("DELETE FROM MEMBERS WHERE schedule_id = :sid")
    override suspend fun clearMemberBySchedule(sid: String)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertMultiMedia(vararg media: Media)

    @Transaction
    @Update
    override suspend fun updateMultiMedia(vararg media: Media)

    @Transaction
    @Delete
    override suspend fun deleteMultiMedia(vararg media: Media)

    @Transaction
    @Query("DELETE FROM MULTI_MEDIA WHERE schedule_id = :sid")
    override suspend fun clearMultiMediaBySchedule(sid: String)
}