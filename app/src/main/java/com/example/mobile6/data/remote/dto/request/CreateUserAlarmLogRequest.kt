package com.example.mobile6.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserAlarmLogRequest(
    val userAlarmId: Long,
    val notifyAt: String,
    val confirmAt: String,
)