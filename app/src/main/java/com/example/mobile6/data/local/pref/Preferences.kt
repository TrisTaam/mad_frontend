package com.example.mobile6.data.local.pref

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class Preferences @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val IS_DOCTOR_SIGNED_IN = "is_doctor_signed_in"
        private const val IS_DOCTOR_MODE = "is_doctor_mode"
    }

    var isDoctorSignedIn: Boolean
        get() = sharedPreferences.getBoolean(IS_DOCTOR_SIGNED_IN, false)
        set(value) {
            sharedPreferences.edit { putBoolean(IS_DOCTOR_SIGNED_IN, value) }
        }

    var isDoctorMode: Boolean
        get() = sharedPreferences.getBoolean(IS_DOCTOR_MODE, false)
        set(value) {
            sharedPreferences.edit { putBoolean(IS_DOCTOR_MODE, value) }
        }
}