package com.example.mobile6.presentation.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.domain.model.Message
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.AuthRepository
import com.example.mobile6.domain.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class ChatBoxUiState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSending: Boolean = false,
    val isDoctorMode: Boolean = false

)

@HiltViewModel
class ChatBoxViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatBoxUiState())
    val uiState: StateFlow<ChatBoxUiState> = _uiState.asStateFlow()

    //    val mode : Boolean = false
    init {
        getMode()
    }

    fun getMode() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val mode = authRepository.isDoctorMode()
            _uiState.update { it.copy(isLoading = false, isDoctorMode = mode) }
        }
    }

    fun getConversation(doctorId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            Timber.d("Getting conversation with doctor: $doctorId")

            when (val result = messageRepository.getConversation(doctorId)) {
                is Resource.Success -> {
                    Timber.d("Got conversation: ${result.data}")
                    val messages = result.data.map { messageResponse ->
                        Message(
                            id = messageResponse.id,
                            senderId = messageResponse.senderId,
                            receiverId = messageResponse.receiverId,
                            content = messageResponse.content,
                            sentAt = messageResponse.sentAt,
                            isFromCurrentUser = messageResponse.senderId != doctorId
                        )
                    }
                    _uiState.update {
                        it.copy(
                            messages = messages,
                            isLoading = false,
                            error = null
                        )
                    }
                }

                is Resource.Error -> {
                    Timber.e("Error getting conversation: ${result.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }

                is Resource.Exception -> {
                    Timber.e("Exception getting conversation: ${result.throwable.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.throwable.message ?: "Có lỗi xảy ra"
                        )
                    }
                }
            }
        }
    }

    fun sendMessage(doctorId: Long, content: String) {
        if (content.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true, error = null) }
            Timber.d("Sending message to doctor $doctorId: $content")

            when (val result = messageRepository.sendMessage(doctorId, content)) {
                is Resource.Success -> {
                    Timber.d("Message sent successfully")
                    val newMessage = Message(
                        id = result.data.id,
                        senderId = result.data.senderId,
                        receiverId = result.data.receiverId,
                        content = result.data.content,
                        sentAt = result.data.sentAt,
                        isFromCurrentUser = result.data.senderId != doctorId
                    )
                    _uiState.update {
                        it.copy(
                            messages = it.messages + newMessage,
                            isSending = false,
                            error = null
                        )
                    }
                }

                is Resource.Error -> {
                    Timber.e("Error sending message: ${result.message}")
                    _uiState.update {
                        it.copy(
                            isSending = false,
                            error = result.message
                        )
                    }
                }

                is Resource.Exception -> {
                    Timber.e("Exception sending message: ${result.throwable.message}")
                    _uiState.update {
                        it.copy(
                            isSending = false,
                            error = result.throwable.message ?: "Có lỗi xảy ra khi gửi tin nhắn"
                        )
                    }
                }
            }
        }
    }
} 