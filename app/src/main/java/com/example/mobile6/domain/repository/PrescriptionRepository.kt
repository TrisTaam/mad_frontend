package com.example.mobile6.domain.repository

import com.example.mobile6.data.remote.dto.request.CreatePrescriptionRequest
import com.example.mobile6.data.remote.dto.response.PrescriptionDetailResponse
import com.example.mobile6.data.remote.dto.response.PrescriptionResponse
import com.example.mobile6.domain.model.Resource

interface PrescriptionRepository {
    suspend fun createPrescription(request: CreatePrescriptionRequest): Resource<PrescriptionResponse>

    suspend fun connectPrescription(id: Long): Resource<PrescriptionResponse>

    suspend fun getPrescription(id: Long): Resource<PrescriptionResponse>

    suspend fun deactivatePrescription(id: Long): Resource<String>

    suspend fun getPrescriptionDetail(id: Long): Resource<PrescriptionDetailResponse>
}