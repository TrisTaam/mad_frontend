package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.response.DoctorResponse
import com.example.mobile6.domain.model.Resource
import retrofit2.http.GET
import retrofit2.http.Query

interface DoctorService {
    @GET("api/v1/doctors")
    suspend fun getSpecialties(): Resource<List<DoctorResponse>>

    @GET("api/v1/doctors")
    suspend fun getDoctorsBySpecialty(@Query("specialty") specialty: String): Resource<List<DoctorResponse>>
}