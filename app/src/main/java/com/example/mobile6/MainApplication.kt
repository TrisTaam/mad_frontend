package com.example.mobile6

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()

        val channel = NotificationChannel(
            "reminder_channel",
            "Reminder Channel",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for medication reminders"
            enableLights(true)
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 500, 1000, 500, 1000)
            setSound(soundUri, audioAttributes)
            setBypassDnd(true)  // Allow notification in DND mode
            lockscreenVisibility =
                android.app.Notification.VISIBILITY_PUBLIC  // Show on lock screen

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setAllowBubbles(true)
            }
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}