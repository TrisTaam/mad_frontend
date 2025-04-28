package com.example.mobile6.data.repository

import com.example.mobile6.data.remote.service.MessageService
import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.domain.model.Message
import com.example.mobile6.domain.model.User
import com.example.mobile6.domain.repository.MessageRepository
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageService: MessageService
) : MessageRepository {
    override suspend fun sendMessage(senderId: Long, receiverId: Long, content: String): Message {
        return messageService.sendMessage(senderId, receiverId, content)
    }

    override suspend fun getConversation(user1Id: Long, user2Id: Long): List<Message> {
        return messageService.getConversation(user1Id, user2Id)
    }

    override suspend fun getAllUsersForDoctor(doctorId: Long): List<User> {
        return messageService.getAllUsersForDoctor(doctorId)
    }

    override suspend fun getAllDoctorsForUser(userId: Long): List<Doctor> {
        return messageService.getAllDoctorsForUser(id = userId, userId = userId)
    }
}