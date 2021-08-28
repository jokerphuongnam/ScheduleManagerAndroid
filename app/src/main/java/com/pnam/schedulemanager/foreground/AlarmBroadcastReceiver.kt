package com.pnam.schedulemanager.foreground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.ui.base.putParcelableExtra

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && context != null) {
            val intentService = Intent(context.applicationContext, AlarmService::class.java)
            intent.getParcelableExtra<Schedule>(SCHEDULE)?.let { user ->
                intentService.putParcelableExtra(SCHEDULE, user)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.applicationContext.startForegroundService(intentService)
            } else {
                context.applicationContext.startService(intentService)
            }
        }
    }

    companion object {
        const val ALARM_ID: String = "alarmId"
        const val SCHEDULE: String = "schedule"
    }
}