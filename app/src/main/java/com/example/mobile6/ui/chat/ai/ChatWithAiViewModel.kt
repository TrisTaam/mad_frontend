package com.example.mobile6.ui.chat.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class ChatWithAiUiState(
    val messages: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSending: Boolean = false
)

@HiltViewModel
class ChatWithAiViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatWithAiUiState())
    val uiState: StateFlow<ChatWithAiUiState> = _uiState.asStateFlow()

    fun sendMessage(question: String) {
        if (question.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true, error = null) }

            // Add user's message to list
            val currentMessages = _uiState.value.messages.toMutableList()
            currentMessages.add("You: $question")
            _uiState.update { it.copy(messages = currentMessages) }

            // Call AI API
            when (val result = messageRepository.chatWithAi(question)) {
                is Resource.Success -> {
                    val response = result.data
                    currentMessages.add("AI: ${response}")
                    _uiState.update {
                        it.copy(
                            messages = currentMessages,
                            isSending = false,
                            error = null
                        )
                    }
                }

                is Resource.Error -> {
                    Timber.e("Error calling AI: ${result.message}")
                    _uiState.update {
                        it.copy(
                            isSending = false,
                            error = result.message
                        )
                    }
                }

                is Resource.Exception -> {
                    Timber.e("Exception calling AI: ${result.throwable.message}")
                    _uiState.update {
                        it.copy(
                            isSending = false,
                            error = result.throwable.message ?: "Có lỗi xảy ra"
                        )
                    }
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(messages = emptyList()) }
    }
}