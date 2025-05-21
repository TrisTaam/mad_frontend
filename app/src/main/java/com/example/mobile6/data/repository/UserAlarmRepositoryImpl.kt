package com.example.mobile6.data.repository

import com.example.mobile6.data.remote.dto.request.CreateUserAlarmRequest
import com.example.mobile6.data.remote.dto.response.UserAlarmResponse
import com.example.mobile6.data.remote.service.UserAlarmService
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.UserAlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserAlarmRepositoryImpl @Inject constructor(
    private val userAlarmService: UserAlarmService
) : UserAlarmRepository {
    override suspend fun createAlarm(createUserAlarmRequest: CreateUserAlarmRequest): Resource<UserAlarmResponse> =
        withContext(Dispatchers.IO) {
            userAlarmService.createUserAlarm(createUserAlarmRequest)
        }

    override suspend fun getUserAlarms(prescriptionDetailId: Long?): Resource<List<UserAlarmResponse>> =
        withContext(Dispatchers.IO) {
            if (prescriptionDetailId != null) {
                userAlarmService.getUserAlarms(prescriptionDetailId)
            } else {
                userAlarmService.getUserAlarms()
            }
        }

    override suspend fun getUserAlarm(id: Long): Resource<UserAlarmResponse> =
        withContext(Dispatchers.IO) {
            userAlarmService.getUserAlarm(id)
        }

    override suspend fun deleteAlarm(id: Long): Resource<String> =
        withContext(Dispatchers.IO) {
            userAlarmService.deleteUserAlarm(id)
        }
}