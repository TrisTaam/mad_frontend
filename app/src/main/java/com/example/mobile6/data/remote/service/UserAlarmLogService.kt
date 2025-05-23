package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.request.CreateUserAlarmLogRequest
import com.example.mobile6.data.remote.dto.response.UserAlarmLogResponse
import com.example.mobile6.domain.model.Resource
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserAlarmLogService {
    @POST("/api/v1/user-alarm-log/")
    suspend fun createUserAlarmLog(@Body request: CreateUserAlarmLogRequest): Resource<UserAlarmLogResponse>

    @GET("/api/v1/user-alarm-log/")
    suspend fun getUserAlarmLogs(): Resource<List<UserAlarmLogResponse>>
}