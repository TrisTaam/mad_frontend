package com.example.mobile6.presentation.ui.chat.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.dto.response.ChatUserInfoResponse
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

data class ChatListUserUiState(
    val users: List<ChatUserInfoResponse> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)


@HiltViewModel
class ChatListUserViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListUserUiState())
    val uiState: StateFlow<ChatListUserUiState> = _uiState.asStateFlow()

    private var allUsers: List<ChatUserInfoResponse> = emptyList()

    init {
        fetchUsersForDoctor()
    }

    fun fetchUsersForDoctor() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            Timber.d("Starting to fetch users")

            when (val result = messageRepository.getAllUsersForDoctor()) {
                is Resource.Success -> {
                    Timber.d("Raw users data: ${result.data}")
                    if (result.data.isEmpty()) {
                        Timber.d("Danh sách người dùng rỗng!")
                    }

                    allUsers = result.data
                    _uiState.update {
                        it.copy(
                            users = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }

                is Resource.Error -> {
                    Timber.d("Lỗi lấy danh sách người dùng: ${result.message}")
                    _uiState.update {
                        it.copy(
                            users = emptyList(),
                            isLoading = false,
                            error = result.message
                        )
                    }
                }

                is Resource.Exception -> {
                    Timber.d("Exception: ${result.throwable.message}")
                    _uiState.update {
                        it.copy(
                            users = emptyList(),
                            isLoading = false,
                            error = result.throwable.message ?: "Có lỗi xảy ra"
                        )
                    }
                }
            }
        }
    }

    fun search(query: String) {
        val filteredUsers = allUsers.filter { user ->
            user.firstName.contains(query, ignoreCase = true) ||
                    user.lastName.contains(query, ignoreCase = true)
        }
        _uiState.update { it.copy(users = filteredUsers) }
    }
}