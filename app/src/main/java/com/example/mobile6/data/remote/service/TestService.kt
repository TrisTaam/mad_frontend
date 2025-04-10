package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.response.TestResponse
import com.example.mobile6.domain.model.Resource
import retrofit2.http.GET

interface TestService {
    @GET("api/v1/test/test3")
    suspend fun test3(): Resource<TestResponse>
}