package com.example.mobile6.domain.repository

import com.example.mobile6.domain.model.Doctor
//import com.example.mobile6.domain.model.User
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.data.remote.dto.response.MessageResponse

interface MessageRepository {
    suspend fun sendMessage(receiverId: Long, content: String): Resource<MessageResponse>
    suspend fun getConversation(user2Id: Long): Resource<List<MessageResponse>>
    //    suspend fun getAllUsersForDoctor(doctorId: Long): Resource<List<User>>
    suspend fun getAllDoctorsForUser(): Resource<List<Doctor>>
}