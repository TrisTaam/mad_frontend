package com.example.mobile6.data.mapper

import com.example.mobile6.data.remote.dto.response.IngredientResponse
import com.example.mobile6.data.remote.dto.response.MedicineResponse
import com.example.mobile6.domain.model.Ingredient
import com.example.mobile6.domain.model.Medicine

fun MedicineResponse.toMedicine(): Medicine {
    return Medicine(
        id = id,
        name = name,
        imgUrl = imgUrl,
        description = description,
        use = use,
        usage = usage,
        precaution = precaution,
        ingredients = ingredients.map { it.toIngredient() }
    )
}

fun IngredientResponse.toIngredient(): Ingredient {
    return Ingredient(
        id = id,
        name = name,
        amount = amount
    )
} 