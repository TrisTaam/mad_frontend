package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.request.CreateAppointmentRequest
import com.example.mobile6.data.remote.dto.response.AppointmentResponse
import com.example.mobile6.domain.model.Resource
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AppointmentService {
    @POST("api/v1/appointment/create")
    suspend fun createAppointment(@Body request: CreateAppointmentRequest): Resource<AppointmentResponse>

    @GET("api/v1/appointment/get-by-user")
    suspend fun getAppointmentsByUser(): Resource<List<AppointmentResponse>>

    @GET("api/v1/appointment/get-by-doctor")
    suspend fun getAppointmentsByDoctor(): Resource<List<AppointmentResponse>>

    @GET("api/v1/appointment/get-by-user-week")
    suspend fun getAppointmentsByUserWeek(
        @Query("startDateTime") startDateTime: String,
        @Query("endDateTime") endDateTime: String
    ): Resource<List<AppointmentResponse>>

    @GET("api/v1/appointment/get-by-doctor-week")
    suspend fun getAppointmentsByDoctorWeek(
        @Query("startDateTime") startDateTime: String,
        @Query("endDateTime") endDateTime: String
    ): Resource<List<AppointmentResponse>>

    @POST("api/v1/appointment/approve")
    suspend fun approveAppointment(
        @Query("id") id: Long
    ): Resource<String>

    @POST("api/v1/appointment/cancel")
    suspend fun cancelAppointment(
        @Query("id") id: Long
    ): Resource<String>
}