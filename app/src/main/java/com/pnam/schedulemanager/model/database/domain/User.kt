package com.pnam.schedulemanager.model.database.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
@Entity(tableName = "users")
data class User(
    @PrimaryKey @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "first_name") var firstName: String,
    @ColumnInfo(name = "last_name") var lastName: String,
    @ColumnInfo(name = "avatar") var avatar: String?,
    @ColumnInfo(name = "gender") var gender: Boolean,
    @ColumnInfo(name = "birth_day") var birthday: Long,
    @ColumnInfo(name = "logins") var logins: List<String>
): Parcelable {
    constructor() : this("", "", "", null, true, 916678800000, mutableListOf())

    val birthdayCalendar: Calendar
        get() {
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeZone = TimeZone.getDefault()
            calendar.timeInMillis = birthday
            return calendar
        }

    var birthdayString: String
        get() {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return sdf.format(birthdayCalendar.time)
        }
        set(value) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getDefault()
            birthday = dateFormat.parse(value)!!.time
        }

    fun clone(): User {
        return this.copy()
    }
}