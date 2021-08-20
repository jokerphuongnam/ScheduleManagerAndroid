package com.pnam.schedulemanager.model.database.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.model.database.domain.Task
import com.pnam.schedulemanager.model.database.domain.User
import com.pnam.schedulemanager.utils.DataConverter
import com.pnam.schedulemanager.model.database.local.impl.RoomSchedulesImpl
import com.pnam.schedulemanager.model.database.local.impl.RoomUserImpl
import com.pnam.schedulemanager.utils.RoomConstrain.DB_VER
import javax.inject.Singleton

@Singleton
@Database(
    entities = [Schedule::class, User::class, Task::class],
    version = DB_VER
)
@TypeConverters(DataConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): RoomUserImpl
    abstract fun noteDao(): RoomSchedulesImpl
}