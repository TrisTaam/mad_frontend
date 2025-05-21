package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.request.CreateUserAlarmRequest
import com.example.mobile6.data.remote.dto.response.UserAlarmResponse
import com.example.mobile6.domain.model.Resource
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserAlarmService {
    @POST("/api/v1/user-alarm/")
    suspend fun createUserAlarm(@Body request: CreateUserAlarmRequest): Resource<UserAlarmResponse>

    @GET("/api/v1/user-alarm/")
    suspend fun getUserAlarms(
        @Query("prescriptionDetailId") prescriptionDetailId: Long? = null
    ): Resource<List<UserAlarmResponse>>

    @GET("/api/v1/user-alarm/{id}")
    suspend fun getUserAlarm(@Path("id") id: Long): Resource<UserAlarmResponse>

    @DELETE("/api/v1/user-alarm/{id}")
    suspend fun deleteUserAlarm(@Path("id") id: Long): Resource<String>
}