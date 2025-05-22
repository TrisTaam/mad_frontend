package com.example.mobile6.domain.repository

import com.example.mobile6.data.remote.dto.response.AppointmentResponse
import com.example.mobile6.domain.model.Resource

interface AppointmentRepository {
    suspend fun createAppointment(
        doctorId: Long,
        appointmentDate: String
    ): Resource<AppointmentResponse>

    suspend fun getAppointmentsByUser(): Resource<List<AppointmentResponse>>

    suspend fun getAppointmentsByDoctor(): Resource<List<AppointmentResponse>>

    suspend fun getAppointmentsByUserWeek(
        startDateTime: String,
        endDateTime: String
    ): Resource<List<AppointmentResponse>>

    suspend fun getAppointmentsByDoctorWeek(
        startDateTime: String,
        endDateTime: String
    ): Resource<List<AppointmentResponse>>

    suspend fun approveAppointment(id: Long): Resource<String>

    suspend fun cancelAppointment(id: Long): Resource<String>
} 