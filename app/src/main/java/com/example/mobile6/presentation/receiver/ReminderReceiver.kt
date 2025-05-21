package com.example.mobile6.presentation.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.mobile6.presentation.service.ReminderService
import com.example.mobile6.presentation.ui.ReminderActivity
import timber.log.Timber
import com.example.mobile6.presentation.ui.util.NotificationManager as CustomNotificationManager

class ReminderReceiver : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_ID_PREFIX = 12300 // Prefix for notification IDs
    }

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("Alarm received!")
        val alarmId = intent.getIntExtra(ReminderActivity.EXTRA_ALARM_ID, -1)
        if (alarmId == -1) {
            Timber.e("Alarm ID not found in intent.")
            return
        }

        // Directly start ReminderActivity regardless of Android version
        val alarmActivityIntent = Intent(context, ReminderActivity::class.java).apply {
            putExtra(ReminderActivity.EXTRA_ALARM_ID, alarmId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        context.startActivity(alarmActivityIntent)

        // Also start foreground service for Android Q+ to maintain system compatibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val serviceIntent = Intent(context, ReminderService::class.java).apply {
                putExtra(ReminderActivity.EXTRA_ALARM_ID, alarmId)
            }
            context.startForegroundService(serviceIntent)
        } else {
            // Show notification as fallback for older devices
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationBuilder = CustomNotificationManager.createFullScreenAlarmNotification(
                context,
                alarmId,
                "reminder_channel"
            )
            notificationManager.notify(
                NOTIFICATION_ID_PREFIX + alarmId,
                notificationBuilder.build()
            )
        }
    }
}