package com.example.mobile6.data.remote.dto.response

import java.io.Serializable

@kotlinx.serialization.Serializable
data class PrescriptionResponse(
    val prescriptionId: Long? = null,
    val userId: Long? = null,
    val userName: String? = null,
    val doctorId: Long?,
    val doctorName: String? = null,
    val prescriptionName: String,
    val prescriptionDate: String,
    val notes: String,
    val status: String,
    val details: List<PrescriptionDetailResponse>
)

@kotlinx.serialization.Serializable
data class PrescriptionDetailResponse(
    val id: Long? = null,
    val prescriptionId: Long? = null,
    val medicineId: Long,
    val medicineName: String? = null,
    val quantity: Int,
    val quantityUnit: String,
    val quantityUsage: String
) : Serializable