package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.request.MedicineInteractionRequest
import com.example.mobile6.data.remote.dto.response.MedicineInteractionResponse
import com.example.mobile6.data.remote.dto.response.MedicineResponse
import com.example.mobile6.domain.model.Resource
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MedicineService {
    @GET("/api/v1/medicine")
    suspend fun getMedicines(): Resource<List<MedicineResponse>>

    @GET("/api/v1/medicine/{id}")
    suspend fun getMedicineById(@Path("id") id: Long): Resource<MedicineResponse>

    @POST("/api/v1/medicine/check-interactions")
    suspend fun checkMedicineInteractions(@Body request: MedicineInteractionRequest): Resource<MedicineInteractionResponse>
} 