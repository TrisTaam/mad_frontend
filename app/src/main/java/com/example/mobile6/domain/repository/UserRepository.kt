package com.example.mobile6.domain.repository

import com.example.mobile6.data.remote.dto.request.UpdateAdditionalUserInfoRequest
import com.example.mobile6.domain.model.Resource

interface UserRepository {
    suspend fun updateAdditionalInformation(request: UpdateAdditionalUserInfoRequest) : Resource<String>
}