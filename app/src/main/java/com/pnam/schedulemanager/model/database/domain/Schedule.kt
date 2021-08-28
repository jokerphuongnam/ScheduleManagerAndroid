package com.pnam.schedulemanager.model.database.domain

import android.os.Parcelable
import androidx.room.*
import com.pnam.schedulemanager.ui.scheduleInfo.ColorsAdapter
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
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
    @ColumnInfo(name = "color") var color: String = ColorsAdapter.ColorElement.YELLOW.rawValue,
    @ColumnInfo(name = "user_id") var userId: String? = null,
    @ColumnInfo(name = "modified_at") var modifiedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "created_at") var createAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "schedule_time") var scheduleTime: Long = System.currentTimeMillis()
) : Parcelable {
    @IgnoredOnParcel
    @Ignore
    var members: List<Member> = mutableListOf()

    @IgnoredOnParcel
    @Ignore
    var tasks: List<Task> = mutableListOf()

    @IgnoredOnParcel
    @Ignore
    var images: List<Media> = mutableListOf()

    @IgnoredOnParcel
    @Ignore
    var audios: List<Media> = mutableListOf()

    @IgnoredOnParcel
    @Ignore
    var videos: List<Media> = mutableListOf()

    val scheduleTimeCalendar: Calendar
        get() {
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeZone = TimeZone.getDefault()
            calendar.timeInMillis = scheduleTime
            return calendar
        }

    var scheduleTimeString: String
        get() {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            return sdf.format(scheduleTimeCalendar.time)
        }
        set(value) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getDefault()
            scheduleTime = dateFormat.parse(value)!!.time
        }
}