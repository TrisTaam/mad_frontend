package com.example.mobile6.presentation.ui

import android.Manifest
import android.app.KeyguardManager
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mobile6.R
import com.example.mobile6.databinding.ActivityReminderBinding
import com.example.mobile6.presentation.service.ReminderService
import com.example.mobile6.presentation.ui.base.BaseActivity
import com.example.mobile6.presentation.ui.util.ReminderScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.Calendar

@AndroidEntryPoint
class ReminderActivity : BaseActivity<ActivityReminderBinding>() {
    companion object {
        const val EXTRA_ALARM_ID = "com.example.nhom6.ALARM_ID"
    }

    private val viewModel: ReminderViewModel by viewModels()

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    private var alarmId: Int = -1
    private lateinit var notifyTime: Calendar

    override val bindingInflater: (LayoutInflater) -> ActivityReminderBinding
        get() = ActivityReminderBinding::inflate

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun initViews() {

        // Ensure activity shows on lock screen
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        keyguardManager.requestDismissKeyguard(this, null)

        alarmId = intent.getIntExtra(EXTRA_ALARM_ID, -1)
        Timber.d("Alarm ID received: $alarmId")

        if (alarmId != -1) {
            viewModel.userAlarmId = alarmId.toLong()
        }

        playAlarmSound()
        vibrateDevice()

        binding.btnDone.setOnClickListener {
            stopAndRescheduleAlarm()
        }
        binding.btnSnooze.setOnClickListener {
            snoozeAlarm()
        }
    }

    private fun playAlarmSound() {
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
            Timber.d("Alarm sound started")
        } catch (e: Exception) {
            Timber.e(e, "Error playing alarm sound")
            e.printStackTrace()
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun vibrateDevice() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        if (vibrator?.hasVibrator() == true) {
            val pattern = longArrayOf(0, 1000, 1000)
            vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))
            Timber.d("Device vibration started")
        } else {
            Timber.w("Device does not have vibrator capability")
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun stopAlarmSoundAndVibration() {
        Timber.d("Stopping alarm sound and vibration")
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        vibrator?.cancel()
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun stopAndRescheduleAlarm() {
        Timber.d("Stop and reschedule alarm with ID: $alarmId")
        stopAlarmSoundAndVibration()
        // Optionally, tell AlarmScheduler to cancel this specific alarm if it's not recurring
        // AlarmScheduler.cancelAlarm(this, alarmId)

        if (::notifyTime.isInitialized) {
            ReminderScheduler.scheduleAlarm(
                this,
                notifyTime.apply {
                    add(Calendar.DAY_OF_YEAR, 1) // Reschedule for the next day
                },
                alarmId
            )
        }

        // If this activity was started by a foreground service, stop the service
        val serviceIntent = Intent(this, ReminderService::class.java)
        stopService(serviceIntent)

        finishAndRemoveTask() // Ensures activity is properly removed
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun snoozeAlarm() {
        Timber.d("Snoozing alarm with ID: $alarmId")
        stopAlarmSoundAndVibration()

        val calendar = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 10) // Snooze for 10 minutes (configurable)
        }

        // Use ReminderScheduler to reschedule the alarm
        ReminderScheduler.scheduleAlarm(this, calendar, alarmId)
        showToast("Nhắc nhở sẽ được báo lại sau 10 phút")

        // If this activity was started by a foreground service, stop the service
        val serviceIntent = Intent(this, ReminderService::class.java)
        stopService(serviceIntent)

        finishAndRemoveTask()
    }

    override fun initObservers() {
        viewModel.uiState.onEach { uiState ->
            if (uiState.isLoggedIn == null) return@onEach
            if (!uiState.isLoggedIn) {
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
            if (uiState.isLoading) {
                return@onEach
            }

            if (uiState.error != null) {
                showToast(uiState.error)
                return@onEach
            }

            binding.tvMedicineName.text = uiState.userAlarm.medicineName
            binding.tvPrescriptionName.text = uiState.userAlarm.prescriptionName
            notifyTime = Calendar.getInstance().apply {
                val hour = uiState.userAlarm.notifyTime.split(":")[0].toInt()
                val minute = uiState.userAlarm.notifyTime.split(":")[1].toInt()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }
        }.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .launchIn(lifecycleScope)
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun onDestroy() {
        super.onDestroy()
        stopAlarmSoundAndVibration()
    }
}