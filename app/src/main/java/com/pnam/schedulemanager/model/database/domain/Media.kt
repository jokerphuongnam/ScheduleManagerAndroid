package com.pnam.schedulemanager.model.database.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pnam.schedulemanager.utils.RetrofitUtils
import kotlinx.parcelize.Parcelize

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
data class Media(
    @PrimaryKey @ColumnInfo(name = "media_id") var mediaId: String = "",
    @ColumnInfo(name = "create_at") var createAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "create_by") var createBy: String = "",
    @ColumnInfo(name = "media_url") var mediaUrl: String = "",
    @ColumnInfo(name = "media_name") var mediaName: String = "",
    @ColumnInfo(name = "mime_type") var mimeType: String = ""
) : Parcelable {
    @ColumnInfo(name = "schedule_id")
    var scheduleId: String = ""

    @ColumnInfo(name = "media_type")
    var mediaType: MediaType = MediaType.IMAGE

    enum class MediaType(val rawValue: String) {
        IMAGE(RetrofitUtils.IMAGES),
        AUDIO(RetrofitUtils.AUDIOS),
        VIDEO(RetrofitUtils.VIDEOS),
        APPLICATION(RetrofitUtils.APPLICATIONS);
    }
}