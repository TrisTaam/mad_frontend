package com.example.mobile6.domain.repository

import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.domain.model.Message
import com.example.mobile6.domain.model.User
import com.example.mobile6.domain.model.Resource

interface MessageRepository {
    suspend fun sendMessage(senderId: Long, receiverId: Long, content: String): Resource<Message>
    suspend fun getConversation(user1Id: Long, user2Id: Long): Resource<List<Message>>
    suspend fun getAllUsersForDoctor(doctorId: Long): Resource<List<User>>
    suspend fun getAllDoctorsForUser(): Resource<List<Doctor>>
}