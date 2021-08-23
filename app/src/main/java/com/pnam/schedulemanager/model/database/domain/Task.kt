package com.pnam.schedulemanager.model.database.domain

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = Schedule::class,
        parentColumns = arrayOf("schedule_id"),
        childColumns = arrayOf("schedule_id"),
        onDelete = CASCADE,
        onUpdate = CASCADE
    )]
)
data class Task(
    @PrimaryKey @ColumnInfo(name = "task_id") var taskId: String = "",
    @ColumnInfo(name = "detail") var detail: String = "",
    @ColumnInfo(name = "finish_at") var finishAt: Long? = null,
    @ColumnInfo(name = "create_at") var createAt: Long = Date().time,
    @ColumnInfo(name = "finish_by") var finishBy: String? = null,
    @ColumnInfo(name = "create_by") var createBy: String = ""
) : Parcelable {
    @ColumnInfo(name = "schedule_id")
    var scheduleId: String = ""
    @Ignore
    var finishByMember: Member? = null
}