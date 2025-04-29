package com.example.mobile6.data.remote.service

import com.example.mobile6.data.remote.dto.request.MessageRequest
import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.domain.model.Message
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MessageService {
    /**
     * Lấy danh sách bác sĩ đã từng tư vấn với user
     */
    @GET("/api/v1/message/getAllDoctors")
    suspend fun getAllDoctorsForUser(): Resource<List<Doctor>>

    /**
     * Gửi tin nhắn
     */
    @POST("/api/v1/message/send")
    suspend fun sendMessage(@Body request: MessageRequest): Resource<Message>

    /**
     * Lấy hội thoại giữa 2 user
     */
    @GET("/api/v1/message/conversation")
    suspend fun getConversation(
        @Query("user1") user1Id: Long,
        @Query("user2") user2Id: Long
    ): Resource<List<Message>>

    /**
     * Lấy danh sách user đã từng tư vấn với bác sĩ
     */
    @GET("/api/v1/message/getAllUsers/")
    suspend fun getAllUsersForDoctor(
        @Path("id") id: Long,
        @Query("doctorId") doctorId: Long
    ): Resource<List<User>>
}
