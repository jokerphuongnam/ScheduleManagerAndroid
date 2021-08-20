package com.pnam.schedulemanager.model.database.domain

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(
    tableName = "schedules",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("user_id"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [
        Index(
            value = ["user_id", "schedule_time"],
            unique = true
        )
    ]
)
data class Schedule(
    @PrimaryKey @ColumnInfo(name = "schedule_id") var scheduleId: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "color") var color: String = "#FBD000",
    @ColumnInfo(name = "user_id") var userId: String? = null,
    @ColumnInfo(name = "modified_at") var modifiedAt: Long = Date().time,
    @ColumnInfo(name = "created_at") var createAt: Long = Date().time,
    @ColumnInfo(name = "schedule_time") var scheduleTime: Long = Date().time
) : Parcelable {
    @Ignore var tasks: MutableList<Task> = mutableListOf()
    @Ignore var images: MutableList<Media> = mutableListOf()
    @Ignore var audios: MutableList<Media> = mutableListOf()
    @Ignore var videos: MutableList<Media> = mutableListOf()
}