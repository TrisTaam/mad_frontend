package com.example.mobile6.data.repository

import com.example.mobile6.data.remote.dto.request.CreateUserAlarmLogRequest
import com.example.mobile6.data.remote.dto.response.UserAlarmLogResponse
import com.example.mobile6.data.remote.service.UserAlarmLogService
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.UserAlarmLogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserAlarmLogRepositoryImpl @Inject constructor(
    private val userAlarmLogService: UserAlarmLogService
) : UserAlarmLogRepository {
    override suspend fun createUserAlarmLog(request: CreateUserAlarmLogRequest): Resource<UserAlarmLogResponse> =
        withContext(Dispatchers.IO) {
            userAlarmLogService.createUserAlarmLog(request)
        }

    override suspend fun getUserAlarmLogs(): Resource<List<UserAlarmLogResponse>> =
        withContext(Dispatchers.IO) {
            userAlarmLogService.getUserAlarmLogs()
        }
}