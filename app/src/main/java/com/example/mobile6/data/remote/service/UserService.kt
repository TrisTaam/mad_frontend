package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.request.UpdateAdditionalUserInfoRequest
import com.example.mobile6.domain.model.Resource
import retrofit2.http.Body
import retrofit2.http.PUT

interface UserService {
    @PUT("/api/v1/user/update-additional")
    suspend fun updateAdditionalInformation(@Body request: UpdateAdditionalUserInfoRequest): Resource<String>
}