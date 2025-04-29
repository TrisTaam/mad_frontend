package com.example.mobile6.domain.model

import kotlinx.serialization.Serializable

/**
 * Doctor model tương ứng với response DoctorInfoResponse ở backend
 */
@Serializable
data class Doctor(
    val specialty: String,
    val firstname: String,
    val lastname: String
)

/**
 * DoctorInfo model tương ứng với entity DoctorInfo ở backend
 */
data class DoctorInfo(
    val id: Long,
    val doctorId: Long,
    val title: String?,
    val description: String?,
    val order: Int?
)
