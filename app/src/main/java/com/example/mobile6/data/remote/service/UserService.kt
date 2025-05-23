package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.request.UpdateAdditionalUserInfoRequest
import com.example.mobile6.data.remote.dto.response.UserResponse
import com.example.mobile6.domain.model.Resource
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface UserService {
    @PUT("/api/v1/user/update-additional")
    suspend fun updateAdditionalInformation(@Body request: UpdateAdditionalUserInfoRequest): Resource<String>

    @Multipart
    @POST("/api/v1/user/update-avatar")
    suspend fun updateAvatar(@Part image: MultipartBody.Part): Resource<String>

    @GET("/api/v1/user/detail-info")
    suspend fun getDetailInfo(): Resource<UserResponse>
}