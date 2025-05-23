package com.example.mobile6.domain.repository

import com.example.mobile6.data.remote.dto.request.CreateUserAlarmRequest
import com.example.mobile6.data.remote.dto.response.UserAlarmResponse
import com.example.mobile6.domain.model.Resource

interface UserAlarmRepository {
    suspend fun createAlarm(createUserAlarmRequest: CreateUserAlarmRequest): Resource<UserAlarmResponse>

    suspend fun getUserAlarms(prescriptionDetailId: Long? = null): Resource<List<UserAlarmResponse>>

    suspend fun getUserAlarm(id: Long): Resource<UserAlarmResponse>

    suspend fun deleteAlarm(id: Long): Resource<String>
}