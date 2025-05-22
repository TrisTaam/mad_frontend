package com.example.mobile6.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class UserAlarmLogResponse(
    val id: Long,
    val userAlarm: UserAlarmResponse,
    val notifyAt: String,
    val confirmAt: String,
)