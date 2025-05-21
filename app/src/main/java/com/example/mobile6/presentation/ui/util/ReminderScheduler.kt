package com.example.mobile6.presentation.ui.util

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.example.mobile6.presentation.receiver.ReminderReceiver
import com.example.mobile6.presentation.ui.ReminderActivity
import timber.log.Timber
import java.util.Calendar

object ReminderScheduler {
    const val ACTION_FIRE_ALARM = "com.example.nhom6.FIRE_ALARM_KT"

    fun scheduleAlarm(context: Context, calendar: Calendar, alarmId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Kiểm tra xem thời gian đã đặt có đã qua chưa
        val currentTime = Calendar.getInstance()
        if (calendar.before(currentTime)) {
            // Nếu thời gian đã qua, đặt báo thức vào ngày hôm sau
            Timber.d("Alarm time is in the past, scheduling for tomorrow")
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        Timber.d("Scheduling alarm for time: ${calendar.time}, ID: $alarmId")

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = ACTION_FIRE_ALARM
            putExtra(ReminderActivity.EXTRA_ALARM_ID, alarmId)
        }

        val pendingIntentFlags =
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            pendingIntentFlags
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                    Timber.d("Alarm scheduled successfully on Android S+")
                } else {
                    Timber.w("Cannot schedule exact alarms - permission not granted")
                    val settingsIntent =
                        Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                    if (settingsIntent.resolveActivity(context.packageManager) != null) {
                        if (context !is Activity) {
                            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(settingsIntent)
                    }
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                Timber.d("Alarm scheduled successfully on Android < S")
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to schedule alarm")
        }
    }

    fun cancelAlarm(context: Context, alarmId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = ACTION_FIRE_ALARM
        }

        val pendingIntentFlags =
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            pendingIntentFlags
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Timber.d("Alarm with ID $alarmId cancelled")
        } else {
            Timber.d("No alarm found with ID $alarmId to cancel")
        }
    }
}