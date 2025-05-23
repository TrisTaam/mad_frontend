package com.example.mobile6.data.repository

import com.example.mobile6.data.remote.dto.request.CreatePrescriptionRequest
import com.example.mobile6.data.remote.dto.response.PrescriptionDetailResponse
import com.example.mobile6.data.remote.dto.response.PrescriptionResponse
import com.example.mobile6.data.remote.service.PrescriptionService
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.PrescriptionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PrescriptionRepositoryImpl @Inject constructor(
    private val prescriptionService: PrescriptionService
) : PrescriptionRepository {

    override suspend fun createPrescription(request: CreatePrescriptionRequest): Resource<PrescriptionResponse> =
        withContext(Dispatchers.IO) {
            prescriptionService.createPrescription(request)
        }

    override suspend fun connectPrescription(id: Long): Resource<PrescriptionResponse> =
        withContext(Dispatchers.IO) {
            prescriptionService.connectPrescription(id)
        }

    override suspend fun getPrescription(id: Long): Resource<PrescriptionResponse> =
        withContext(Dispatchers.IO) {
            prescriptionService.getPrescription(id)
        }

    override suspend fun deactivatePrescription(id: Long): Resource<String> =
        withContext(Dispatchers.IO) {
            prescriptionService.deactivatePrescription(id)
        }

    override suspend fun getPrescriptionDetail(id: Long): Resource<PrescriptionDetailResponse> =
        withContext(Dispatchers.IO) {
            prescriptionService.getPrescriptionDetail(id)
        }

    override suspend fun getPrescriptionsForUser(): Resource<List<PrescriptionResponse>> =
        withContext(Dispatchers.IO) {
            prescriptionService.getPrescriptionsForUser()
        }

    override suspend fun getPrescriptionsForDoctor(): Resource<List<PrescriptionResponse>> =
        withContext(Dispatchers.IO) {
            prescriptionService.getPrescriptionsForDoctor()
        }
}