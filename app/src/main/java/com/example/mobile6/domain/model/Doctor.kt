package com.example.mobile6.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Doctor(
    val id: Long,
    val specialty: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val phoneNumber: String?,
    val gender: String?,
    val dateOfBirth: String?,
    val weight: Int?,
    val height: Int?,
    val role: String?,
    val avatar: String?,
    val doctorInfos: List<DoctorInfo>?
) : Parcelable

