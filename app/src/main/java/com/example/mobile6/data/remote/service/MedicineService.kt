package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.response.MedicineResponse
import com.example.mobile6.data.remote.dto.response.NetworkResponse
import com.example.mobile6.domain.model.Resource
import retrofit2.http.GET
import retrofit2.http.Path

interface MedicineService {
    @GET("/api/v1/medicines")
    suspend fun getMedicines(): Resource<List<MedicineResponse>>
    
    @GET("/api/v1/medicines/{id}")
    suspend fun getMedicineById(@Path("id") id: Long): Resource<MedicineResponse>
} 