package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.request.CreatePrescriptionRequest
import com.example.mobile6.data.remote.dto.response.PrescriptionDetailResponse
import com.example.mobile6.data.remote.dto.response.PrescriptionResponse
import com.example.mobile6.domain.model.Resource
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PrescriptionService {
    @POST("/api/v1/prescription/")
    suspend fun createPrescription(@Body request: CreatePrescriptionRequest): Resource<PrescriptionResponse>

    @POST("/api/v1/{id}/connect")
    suspend fun connectPrescription(@Path("id") id: Long): Resource<PrescriptionResponse>

    @GET("/api/v1/{id}")
    suspend fun getPrescription(@Path("id") id: Long): Resource<PrescriptionResponse>

    @GET("/api/v1/{id}/deactivate")
    suspend fun deactivatePrescription(@Path("id") id: Long): Resource<String>

    @GET("/api/v1/detail/{id}")
    suspend fun getPrescriptionDetail(@Path("id") id: Long): Resource<PrescriptionDetailResponse>
}