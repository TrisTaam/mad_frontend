package com.example.mobile6.presentation.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.mobile6.presentation.ui.ReminderActivity
import timber.log.Timber
import com.example.mobile6.presentation.ui.util.NotificationManager as CustomNotificationManager

class ReminderService : Service() {
    companion object {
        private const val SERVICE_NOTIFICATION_ID = 4567 // Unique ID for service notification
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("Foreground service created.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("Foreground service started.")
        val alarmId = intent?.getIntExtra(ReminderActivity.EXTRA_ALARM_ID, -1) ?: -1

        if (alarmId == -1) {
            Timber.e("Alarm ID not found in service intent. Stopping service.")
            stopSelf()
            return START_NOT_STICKY
        }

        // Directly start ReminderActivity
        val alarmActivityIntent = Intent(this, ReminderActivity::class.java).apply {
            putExtra(ReminderActivity.EXTRA_ALARM_ID, alarmId)
            val flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            setFlags(flags)
        }
        startActivity(alarmActivityIntent)

        // Create notification for foreground service requirement
        val notificationBuilder = CustomNotificationManager.createFullScreenAlarmNotification(
            this,
            alarmId,
            "reminder_channel"
        )
        val notification = notificationBuilder
            .setOngoing(true)
            .setContentText("Alarm ID: $alarmId - Tap to manage.")
            .build()

        try {
            startForeground(SERVICE_NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Timber.e("Error starting foreground service $e")
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("Foreground service destroyed.")
        stopForeground(STOP_FOREGROUND_REMOVE)
    }
}