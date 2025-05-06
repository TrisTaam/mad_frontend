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
    
    override suspend fun createAppointment(doctorId: Long, appointmentDate: String): Resource<AppointmentResponse> {
        val request = CreateAppointmentRequest(doctorId, appointmentDate)
        return appointmentService.createAppointment(request)
    }
} 