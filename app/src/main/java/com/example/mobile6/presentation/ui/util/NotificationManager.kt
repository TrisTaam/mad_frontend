package com.example.mobile6.presentation.ui.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.mobile6.R
import com.example.mobile6.presentation.ui.ReminderActivity

object NotificationManager {
    fun createFullScreenAlarmNotification(
        context: Context,
        alarmId: Int,
        channelId: String
    ): NotificationCompat.Builder {
        val alarmActivityIntent = Intent(context, ReminderActivity::class.java).apply {
            putExtra(ReminderActivity.EXTRA_ALARM_ID, alarmId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntentFlags =
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            alarmId, // Request code, ensure uniqueness for each alarm
            alarmActivityIntent,
            pendingIntentFlags
        )

        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_pill)
            .setContentTitle("Alarm Ringing!")
            .setContentText("Alarm ID: $alarmId")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    }

//    fun showReminderNotification(
//        context: Context,
//        title: String,
//        message: String,
//        medicineId: Int,
//        activityToOpen: Class<*>
//    ) {
//        // Create Intent for opening the activity with proper flags
//        val intent = Intent(context, activityToOpen).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
//                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP
//            putExtra("title", title)
//            putExtra("message", message)
//            putExtra("medicineId", medicineId)
//            // Add a unique data URI to ensure the intent is unique
//            // This helps to start the activity even if the app is killed
//            data = "reminder://${System.currentTimeMillis()}".toUri()
//        }
//
//        Timber.d("Creating notification with medicineId: $medicineId")
//
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            medicineId, // Use medicineId to make it uniquely identifiable
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        // Snooze action
//        val snoozeIntent = Intent(context, ReminderReceiver::class.java).apply {
//            action = ReminderReceiver.ACTION_SNOOZE
//            putExtra("title", title)
//            putExtra("message", message)
//            putExtra("medicineId", medicineId)
//        }
//        val snoozePendingIntent = PendingIntent.getBroadcast(
//            context,
//            medicineId + 1000, // Add offset to make it unique
//            snoozeIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        // Done action
//        val doneIntent = Intent(context, ReminderReceiver::class.java).apply {
//            action = ReminderReceiver.ACTION_DONE
//            putExtra("medicineId", medicineId)
//        }
//        val donePendingIntent = PendingIntent.getBroadcast(
//            context,
//            medicineId + 2000, // Add offset to make it unique
//            doneIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
//
//        val audioAttributes = AudioAttributes.Builder()
//            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//            .setUsage(AudioAttributes.USAGE_ALARM)
//            .build()
//
//        val notification = NotificationCompat.Builder(context, "reminder_channel")
//            .setSmallIcon(R.drawable.ic_pill)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setPriority(NotificationCompat.PRIORITY_MAX) // Use MAX priority
//            .setCategory(NotificationCompat.CATEGORY_ALARM)
//            .setSound(soundUri)
//            .setVibrate(longArrayOf(0, 500, 1000, 500, 1000)) // Strong vibration pattern
//            .setContentIntent(pendingIntent)
//            .setFullScreenIntent(pendingIntent, true) // Important for showing when app is killed
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Show on lock screen
//            .setOngoing(true) // Make it persistent
//            .setAutoCancel(false) // Don't dismiss on tap
//            .addAction(R.drawable.ic_snooze, "Trì hoãn", snoozePendingIntent)
//            .addAction(R.drawable.ic_pill, "Uống thuốc", donePendingIntent)
//            .build()
//
//        val manager = context.getSystemService(
//            NotificationManager::class.java
//        )
//
//        // Force DND mode to allow this critical alarm notification
//        notification.flags = notification.flags or android.app.Notification.FLAG_INSISTENT
//
//        manager.notify(medicineId, notification) // Use medicineId as notification ID
//    }
//
//    private fun getNotificationId(): Int = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
}