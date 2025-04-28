package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.response.DoctorResponse
import com.example.mobile6.domain.model.Resource
import retrofit2.http.GET

interface DoctorService {
    @GET("api/v1/doctors")
    suspend fun getSpecialties(): Resource<List<DoctorResponse>>
}