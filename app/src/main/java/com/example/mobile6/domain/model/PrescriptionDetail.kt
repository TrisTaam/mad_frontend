package com.example.mobile6.domain.model

data class PrescriptionDetail(
    val id: Long,
    val prescriptionId: Long,
    val medicineId: Long,
    val quantity: Int,
    val quantityUnit: String,
    val quantityUsage: String
)