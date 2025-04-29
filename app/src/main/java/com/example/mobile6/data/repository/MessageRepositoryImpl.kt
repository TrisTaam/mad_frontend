package com.example.mobile6.data.repository

import com.example.mobile6.data.remote.dto.request.MessageRequest
import com.example.mobile6.data.remote.service.MessageService
import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.domain.model.Message
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.model.User
import com.example.mobile6.domain.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageService: MessageService
) : MessageRepository {
    override suspend fun sendMessage(senderId: Long, receiverId: Long, content: String): Resource<Message> =
        withContext(Dispatchers.IO) {
            messageService.sendMessage(MessageRequest(senderId, receiverId, content))
        }

    override suspend fun getConversation(user1Id: Long, user2Id: Long): Resource<List<Message>> =
        withContext(Dispatchers.IO) {
            messageService.getConversation(user1Id, user2Id)
        }

    override suspend fun getAllUsersForDoctor(doctorId: Long): Resource<List<User>> =
        withContext(Dispatchers.IO) {
            messageService.getAllUsersForDoctor(id = doctorId, doctorId = doctorId)
        }

    override suspend fun getAllDoctorsForUser(): Resource<List<Doctor>> =
        withContext(Dispatchers.IO) {
            messageService.getAllDoctorsForUser()
        }
}