package com.example.mobile6.domain.repository

import com.example.mobile6.data.remote.dto.request.UpdateAdditionalUserInfoRequest
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.model.User
import java.io.File

interface UserRepository {
    suspend fun updateAdditionalInformation(request: UpdateAdditionalUserInfoRequest): Resource<String>
    suspend fun updateAvatar(imageFile: File): Resource<String>
    suspend fun getDetailInfo(): Resource<User>
}