package com.pnam.schedulemanager.foreground

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.pnam.schedulemanager.R
import com.pnam.schedulemanager.model.database.domain.Schedule
import com.pnam.schedulemanager.ui.ring.RingActivity

class AlarmService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm).apply {
            isLooping = true
        }

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val notificationIntent = Intent(applicationContext, RingActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
            intent.getParcelableExtra<Schedule>(AlarmBroadcastReceiver.SCHEDULE)?.let { schedule ->
                notificationIntent.putExtra(AlarmBroadcastReceiver.SCHEDULE, schedule)
                val alarmTitle = String.format("%s Schedule", schedule.title)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel()
                }

                val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(alarmTitle)
                    .setContentText("Ring Ring .. Ring Ring")
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setContentIntent(pendingIntent)
                    .build()

                mediaPlayer.setVolume(1F, 1F)
                mediaPlayer.start()
                vibrator.vibrate(longArrayOf(0, 100, 1000), 0)

                startForeground(
                    schedule.scheduleTime.toInt(),
                    notification
                )
            }
        }
        return START_REDELIVER_INTENT
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_ID,
            NotificationManager.IMPORTANCE_NONE
        )
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return CHANNEL_ID
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        vibrator.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val CHANNEL_ID: String = "ALARM_SERVICE_CHANNEL"
    }
}