package com.example.mobile6.domain.model

import java.util.Date

data class Prescription(
    val id: Long,
    val userId: Long,
    val doctorId: Long,
    val prescriptionName: String,
    val prescriptionDate: Date,
    val notes: String,
    val status: String,
)