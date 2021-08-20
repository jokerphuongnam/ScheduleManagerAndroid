package com.pnam.schedulemanager.model.database.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(
    tableName = "multi_media",
    foreignKeys = [ForeignKey(
        entity = Schedule::class,
        parentColumns = arrayOf("schedule_id"),
        childColumns = arrayOf("schedule_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
open class Media(
    @PrimaryKey @ColumnInfo(name = "media_id") open var mediaId: String = "",
    @ColumnInfo(name = "create_at") open var createAt: Long = Date().time,
    @ColumnInfo(name = "create_by") open var createBy: String = "",
    @ColumnInfo(name = "media_url") open var mediaUrl: String = ""
) : Parcelable {
    @ColumnInfo(name = "schedule_id")
    var scheduleId: String = ""

    @ColumnInfo(name = "media_type")
    var mediaType: MediaType = MediaType.IMAGE

    enum class MediaType(val rawValue: Int) {
        IMAGE(0), AUDIO(1), VIDEO(2);
    }
}