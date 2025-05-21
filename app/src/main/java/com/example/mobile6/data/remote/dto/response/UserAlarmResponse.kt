package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class UserAlarmResponse(
    val id: Long,
    val userId: Long,
    val prescriptionId: Long,
    val prescriptionName: String,
    val medicineId: Long,
    val medicineName: String,
    val notifyTime: String,
)