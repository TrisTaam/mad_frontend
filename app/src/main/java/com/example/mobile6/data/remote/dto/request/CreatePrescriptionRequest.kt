package com.example.mobile6.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreatePrescriptionRequest(
    val prescriptionName: String,
    val prescriptionDate: String,
    val notes: String,
    val details: List<CreatePrescriptionDetailRequest>
)

@Serializable
data class CreatePrescriptionDetailRequest(
    val medicineId: Long,
    val quantity: Int,
    val quantityUnit: String,
    val quantityUsage: String
)