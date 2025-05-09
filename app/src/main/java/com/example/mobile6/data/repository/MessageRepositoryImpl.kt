package com.example.mobile6.data.repository

import com.example.mobile6.data.mapper.toDoctor
import com.example.mobile6.data.remote.dto.request.MessageRequest
import com.example.mobile6.data.remote.dto.response.MessageListResponse
import com.example.mobile6.data.remote.service.MessageService
import com.example.mobile6.data.remote.util.map
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageService: MessageService
) : MessageRepository {
    override suspend fun sendMessage(receiverId: Long, content: String): Resource<MessageListResponse> =
        withContext(Dispatchers.IO) {
            try {
                messageService.sendMessage(MessageRequest(receiverId = receiverId, content = content))
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error occurred")
            }
        }

    override suspend fun getConversation(user2Id: Long): Resource<MessageListResponse> =
        withContext(Dispatchers.IO) {
            try {
                messageService.getConversation(user2Id)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error occurred")
            }
        }

//    override suspend fun getAllUsersForDoctor(doctorId: Long): Resource<List<User>> =
//        withContext(Dispatchers.IO) {
//            try {
//                messageService.getAllUsersForDoctor(id = doctorId, doctorId = doctorId)
//            } catch (e: Exception) {
//                Resource.Error(e.message ?: "Unknown error occurred")
//            }
//        }

    override suspend fun getAllDoctorsForUser(): Resource<List<Doctor>> =
        withContext(Dispatchers.IO) {
            messageService.getAllDoctorsForUser()
                .onError { message, code ->
                    Timber.e("Lỗi khi lấy danh sách bác sĩ: $message, code: $code")
                }
                .onException { e ->
                    Timber.e(e, "Ngoại lệ khi lấy danh sách bác sĩ")
                }
                .map { doctors ->
                    doctors.map { it.toDoctor() }
                }
        }
}