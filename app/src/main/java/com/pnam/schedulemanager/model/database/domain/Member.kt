package com.pnam.schedulemanager.model.database.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "members",
    foreignKeys = [ForeignKey(
        entity = Schedule::class,
        parentColumns = arrayOf("schedule_id"),
        childColumns = arrayOf("schedule_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
class Member(
    @PrimaryKey @ColumnInfo(name = "member_id") var memberId: String = "",
    @ColumnInfo(name = "avatar") var avatar: String? = null,
    @ColumnInfo(name = "join_at") var joinAt: Long = 0,
    @ColumnInfo(name = "first_name") var firstName: String = "",
    @ColumnInfo(name = "last_name") var lastName: String = "",
    @ColumnInfo(name = "add_by_or_founder") var addByOrFounder: String? = null
) : Parcelable {
    @ColumnInfo(name = "schedule_id")
    var scheduleId: String = ""

    val fullName: String get() = "$firstName $lastName"
}