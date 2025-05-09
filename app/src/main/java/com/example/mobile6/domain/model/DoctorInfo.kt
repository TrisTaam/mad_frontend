package com.example.mobile6.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DoctorInfo(
    val id: Long,
    val title: String,
    val description: String,
    val order: Int
) : Parcelable 