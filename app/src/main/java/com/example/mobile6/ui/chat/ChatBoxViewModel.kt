package com.example.mobile6.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.dto.response.MessageResponse
import com.example.mobile6.domain.model.Message
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatBoxUiState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSending: Boolean = false
)

@HiltViewModel
class ChatBoxViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatBoxUiState())
    val uiState: StateFlow<ChatBoxUiState> = _uiState.asStateFlow()

    fun getConversation(doctorId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            Log.d("ChatBoxViewModel", "Getting conversation with doctor: $doctorId")
            
            when (val result = messageRepository.getConversation(doctorId)) {
                is Resource.Success -> {
                    Log.d("ChatBoxViewModel", "Got conversation: ${result.data}")
                    val messages = result.data.map { messageResponse ->
                        Message(
                            id = messageResponse.id,
                            senderId = messageResponse.senderId,
                            receiverId = messageResponse.receiverId,
                            content = messageResponse.content,
                            sentAt = messageResponse.sentAt,
                            isFromCurrentUser = messageResponse.senderId == getCurrentUserId()
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
                    Log.e("ChatBoxViewModel", "Error getting conversation: ${result.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                
                is Resource.Exception -> {
                    Log.e("ChatBoxViewModel", "Exception getting conversation: ${result.throwable.message}")
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
            Log.d("ChatBoxViewModel", "Sending message to doctor $doctorId: $content")
            
            when (val result = messageRepository.sendMessage(doctorId, content)) {
                is Resource.Success -> {
                    Log.d("ChatBoxViewModel", "Message sent successfully")
                    val newMessage = Message(
                        id = result.data.id,
                        senderId = result.data.senderId,
                        receiverId = result.data.receiverId,
                        content = result.data.content,
                        sentAt = result.data.sentAt,
                        isFromCurrentUser = result.data.senderId == getCurrentUserId()
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
                    Log.e("ChatBoxViewModel", "Error sending message: ${result.message}")
                    _uiState.update {
                        it.copy(
                            isSending = false,
                            error = result.message
                        )
                    }
                }
                
                is Resource.Exception -> {
                    Log.e("ChatBoxViewModel", "Exception sending message: ${result.throwable.message}")
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

    private fun getCurrentUserId(): Long {
        // TODO: Implement getting current user ID from UserPreferences or similar
        return 0L
    }
} 