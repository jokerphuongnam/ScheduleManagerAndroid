package com.pnam.schedulemanager.model.database.local.impl

import androidx.room.*
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.database.domain.supportquery.ScheduleWithTasks
import com.pnam.schedulemanager.model.database.local.SchedulesLocal

@Dao
interface RoomSchedulesImpl : SchedulesLocal {
    /**
     * find all note for test
     * */
    @Query("SELECT schedule_id, title, description, created_at, color, modified_at, schedule_time, user_id FROM SCHEDULES ORDER BY schedule_id")
    override suspend fun findSchedules(): MutableList<Schedule>

    /**
     * get first note for init notes
     * */
    @Query("SELECT  schedule_id, title, description, created_at, color, modified_at, schedule_time, user_id FROM SCHEDULES WHERE user_id = :uid ORDER BY created_at DESC LIMIT 1")
    override suspend fun findLastUpdateSingle(uid: String): Schedule

    /**
     * when user select note user need seen all info note
     * */
    @Transaction
    @Query("SELECT * FROM SCHEDULES WHERE schedule_id = :sid")
    suspend fun findNotesWithTask(sid: String): ScheduleWithTasks

    /**
     * make noteWithTask do support query for note
     * */
    override suspend fun findSingleSchedule(sid: String): Schedule = findNotesWithTask(sid).toNote()


    @Query("SELECT * FROM TASKS WHERE schedule_id = :sid")
    override suspend fun findTasksByUid(sid: String): List<Task>

    /**
     * save create time save for query (if outdated will refresh)
     * */
    override suspend fun insertSchedulesWithTime(schedules: List<Schedule>) {
        schedules.map { note ->
            note.apply {
                createAt = System.currentTimeMillis()
                modifiedAt = System.currentTimeMillis()
            }
        }
        insertSchedules(schedules)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertSchedules(schedules: List<Schedule>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertTasks(vararg tasks: Task)

    @Update
    override suspend fun updateSchedules(vararg schedules: Schedule): Int

    @Update
    override suspend fun updateTasks(vararg tasks: Task): Int

    @Delete
    override suspend fun deleteSchedules(vararg schedules: Schedule)

    @Query("DELETE FROM SCHEDULES WHERE user_id = :uid")
    override suspend fun clearSchedulesByUserId(uid: String)

    @Delete
    override suspend fun deleteTasks(vararg tasks: Task)

    @Query("DELETE FROM TASKS WHERE schedule_id = :sid")
    override suspend fun clearTasksBySchedule(sid: String): Int
}