package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.request.CreateAppointmentRequest
import com.example.mobile6.data.remote.dto.response.AppointmentResponse
import com.example.mobile6.domain.model.Resource
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AppointmentService {
    @POST("api/v1/appointment/create")
    suspend fun createAppointment(@Body request: CreateAppointmentRequest): Resource<AppointmentResponse>

    @GET("api/v1/appointment/get-by-user")
    suspend fun getAppointmentsByUser(): Resource<List<AppointmentResponse>>

    @GET("api/v1/appointment/get-by-doctor")
    suspend fun getAppointmentsByDoctor(): Resource<List<AppointmentResponse>>
}