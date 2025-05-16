package com.example.mobile6.data.repository

import com.example.mobile6.data.mapper.toDoctor
import com.example.mobile6.data.remote.dto.request.MessageAIRequest
import com.example.mobile6.data.remote.dto.request.MessageRequest
import com.example.mobile6.data.remote.dto.response.ChatUserInfoResponse
import com.example.mobile6.data.remote.dto.response.MessageResponse
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
    override suspend fun sendMessage(receiverId: Long, content: String): Resource<MessageResponse> =
        withContext(Dispatchers.IO) {
            messageService.sendMessage(MessageRequest(receiverId = receiverId, content = content))
                .onError { message, code ->
                    Timber.e("Lỗi khi gửi tin nhắn: $message, code: $code")
                }
                .onException { e ->
                    Timber.e(e, "Ngoại lệ khi gửi tin nhắn")
                }
        }

    override suspend fun getConversation(user2Id: Long): Resource<List<MessageResponse>> =
        withContext(Dispatchers.IO) {
            messageService.getConversation(user2Id)
                .onError { message, code ->
                    Timber.e("Lỗi khi tải tin nhắn: $message, code: $code")
                }
                .onException { e ->
                    Timber.e(e, "Ngoại lệ khi tải tin nhắn")
                }
        }

    override suspend fun getAllUsersForDoctor(): Resource<List<ChatUserInfoResponse>> =
        withContext(Dispatchers.IO) {
            messageService.getAllUsersForDoctor()
                .onError { message, code ->
                    Timber.e("Lỗi khi lấy danh sách người dùng: $message, code: $code")
                }.onException { e ->
                    Timber.e(e, "Ngoại lệ khi lấy danh sách người dùng")
                }
        }

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

    override suspend fun chatWithAi(question: String): Resource<String> =
        withContext(Dispatchers.IO) {
            val request = MessageAIRequest(
                model = "gpt-4o-mini",
                messages = listOf(
                    MessageAIRequest.Message(
                        role = "user",
                        content = question
                    )
                )
            )
            messageService.chatWithAi(request)
                .onError { message, code ->
                    Timber.e("Lỗi khi gọi AI: $message, code: $code")
                }
                .onException { e ->
                    Timber.e(e, "Ngoại lệ khi gọi AI")
                }
        }
}