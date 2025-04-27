package com.example.mobile6.data.mapper

import com.example.mobile6.data.remote.dto.response.MedicineInteractionResponse
import com.example.mobile6.domain.model.MedicineInteraction

fun MedicineInteractionResponse.toMedicineInteraction(): MedicineInteraction {
    return MedicineInteraction(
        hasInteraction = hasInteraction,
        interactionDetails = interactionDetails
    )
} 