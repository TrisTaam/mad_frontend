package com.example.mobile6.domain.repository

//import com.example.mobile6.domain.model.User
import com.example.mobile6.data.remote.dto.response.ChatUserInfoResponse
import com.example.mobile6.data.remote.dto.response.MessageResponse
import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.domain.model.Resource

interface MessageRepository {
    suspend fun sendMessage(receiverId: Long, content: String): Resource<MessageResponse>
    suspend fun getConversation(user2Id: Long): Resource<List<MessageResponse>>
    suspend fun getAllUsersForDoctor(): Resource<List<ChatUserInfoResponse>>
    suspend fun getAllDoctorsForUser(): Resource<List<Doctor>>
    suspend fun chatWithAi(question: String): Resource<String>
}