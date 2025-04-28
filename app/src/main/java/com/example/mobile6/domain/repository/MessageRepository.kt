package com.example.mobile6.domain.repository

import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.domain.model.Message
import com.example.mobile6.domain.model.User

interface MessageRepository {
    suspend fun sendMessage(senderId: Long, receiverId: Long, content: String): Message
    suspend fun getConversation(user1Id: Long, user2Id: Long): List<Message>
    suspend fun getAllUsersForDoctor(doctorId: Long): List<User>
    suspend fun getAllDoctorsForUser(userId: Long): List<Doctor>
}