package com.example.mobile6.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserAlarmRequest(
    val prescriptionDetailId: Long,
    val notifyTime: String,
)