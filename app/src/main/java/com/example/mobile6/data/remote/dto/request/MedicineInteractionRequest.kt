package com.example.mobile6.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class MedicineInteractionRequest(
    val newMedicineId: Long,
    val existingMedicineIds: List<Long>
) 