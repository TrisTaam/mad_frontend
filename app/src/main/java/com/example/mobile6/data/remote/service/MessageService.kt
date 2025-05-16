package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.request.MessageAIRequest
import com.example.mobile6.data.remote.dto.request.MessageRequest
import com.example.mobile6.data.remote.dto.response.ChatDoctorInfoResponse
import com.example.mobile6.data.remote.dto.response.ChatUserInfoResponse
import com.example.mobile6.data.remote.dto.response.MessageResponse
import com.example.mobile6.domain.model.Resource
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageService {
    /**
     * Lấy danh sách bác sĩ đã từng tư vấn với user
     */
    @GET("/api/v1/message/getAllDoctors")
    suspend fun getAllDoctorsForUser(): Resource<List<ChatDoctorInfoResponse>>

    /**
     * Gửi tin nhắn
     */
    @POST("/api/v1/message/send")
    suspend fun sendMessage(@Body request: MessageRequest): Resource<MessageResponse>

    /**
     * Lấy hội thoại giữa 2 user
     */
    @GET("/api/v1/message/conversation")
    suspend fun getConversation(
        @Query("user2") user2Id: Long
    ): Resource<List<MessageResponse>>

    /**
     * Lấy danh sách user đã từng tư vấn với bác sĩ
     */
    @GET("/api/v1/message/getAllUsersForDoctor")
    suspend fun getAllUsersForDoctor(): Resource<List<ChatUserInfoResponse>>

    @POST("/api/v1/message/chat-with-ai")
    suspend fun chatWithAi(@Body request: MessageAIRequest): Resource<String>
}