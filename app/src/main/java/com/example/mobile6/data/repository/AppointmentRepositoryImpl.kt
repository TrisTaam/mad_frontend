package com.example.mobile6.data.repository

import com.example.mobile6.data.remote.dto.request.CreateAppointmentRequest
import com.example.mobile6.data.remote.dto.response.AppointmentResponse
import com.example.mobile6.data.remote.service.AppointmentService
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.AppointmentRepository
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(
    private val appointmentService: AppointmentService
) : AppointmentRepository {

    override suspend fun createAppointment(
        doctorId: Long,
        appointmentDate: String
    ): Resource<AppointmentResponse> {
        val request = CreateAppointmentRequest(doctorId, appointmentDate)
        return appointmentService.createAppointment(request)
    }

    override suspend fun getAppointmentsByUser(): Resource<List<AppointmentResponse>> {
        return appointmentService.getAppointmentsByUser()
    }

    override suspend fun getAppointmentsByDoctor(): Resource<List<AppointmentResponse>> {
        return appointmentService.getAppointmentsByDoctor()
    }

    override suspend fun getAppointmentsByUserWeek(
        startDateTime: String,
        endDateTime: String
    ): Resource<List<AppointmentResponse>> {
        return appointmentService.getAppointmentsByUserWeek(startDateTime, endDateTime)
    }

    override suspend fun getAppointmentsByDoctorWeek(
        startDateTime: String,
        endDateTime: String
    ): Resource<List<AppointmentResponse>> {
        return appointmentService.getAppointmentsByDoctorWeek(startDateTime, endDateTime)
    }

    override suspend fun approveAppointment(id: Long): Resource<String> {
        return appointmentService.approveAppointment(id)
    }

    override suspend fun cancelAppointment(id: Long): Resource<String> {
        return appointmentService.cancelAppointment(id)
    }
} 