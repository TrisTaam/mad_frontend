package com.example.mobile6.domain.repository

import com.example.mobile6.data.remote.dto.request.CreateUserAlarmLogRequest
import com.example.mobile6.data.remote.dto.response.UserAlarmLogResponse
import com.example.mobile6.domain.model.Resource

interface UserAlarmLogRepository {
    suspend fun createUserAlarmLog(request: CreateUserAlarmLogRequest): Resource<UserAlarmLogResponse>

    suspend fun getUserAlarmLogs(): Resource<List<UserAlarmLogResponse>>
}