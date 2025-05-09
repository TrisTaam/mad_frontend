package com.example.mobile6.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.domain.model.Message
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ChatBoxViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage

    private fun showUiMessage(message: String) {
        _uiMessage.value = message
    }

    fun resetUiMessage() {
        _uiMessage.value = null
    }

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _currentUserId = MutableStateFlow<Long?>(null)
    val currentUserId: StateFlow<Long?> = _currentUserId

    private val _receiverId = MutableStateFlow<Long?>(null)

    private val refreshInterval = 1000L * 60 // 1 minute

    init {
        viewModelScope.launch {
            while (true) {
                delay(refreshInterval)
                _receiverId.value?.let { receiverId ->
                    fetchConversation(receiverId)
                }
            }
        }
    }

    fun setCurrentUserId(userId: Long) {
        _currentUserId.value = userId
    }

    fun setReceiverId(receiverId: Long) {
        if (receiverId <= 0) {
            showUiMessage("Invalid receiver ID")
            return
        }
        _receiverId.value = receiverId
        fetchConversation(receiverId)
    }

    private fun fetchConversation(receiverId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = messageRepository.getConversation(receiverId)
                if (response is Resource.Success) {
                    _messages.value = response.data.data.map { messageData ->
                        Message(
                            id = messageData.id,
                            senderId = messageData.senderId,
                            receiverId = messageData.receiverId,
                            content = messageData.content,
                            sentAt = LocalDateTime.parse(
                                messageData.sentAt,
                                DateTimeFormatter.ISO_DATE_TIME
                            )
                        )
                    }
                } else {
                    showUiMessage("Failed to fetch conversation")
                }
            } catch (e: Exception) {
                showUiMessage("Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sendMessage(content: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val receiverId = _receiverId.value
                if (receiverId == null || receiverId <= 0) {
                    showUiMessage("Invalid receiver ID")
                    return@launch
                }
                
                val response = messageRepository.sendMessage(receiverId, content)
                if (response is Resource.Success) {
                    // Refresh conversation after sending
                    fetchConversation(receiverId)
                } else {
                    showUiMessage("Failed to send message")
                }
            } catch (e: Exception) {
                showUiMessage("Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}