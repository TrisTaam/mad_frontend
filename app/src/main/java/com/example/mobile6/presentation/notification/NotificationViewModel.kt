package com.example.mobile6.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.dto.response.UserAlarmLogResponse
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.repository.UserAlarmLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val userAlarmLogRepository: UserAlarmLogRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    init {
        getUserAlarmLog()
    }

    fun getUserAlarmLog() {
        viewModelScope.launch {
            userAlarmLogRepository.getUserAlarmLogs()
                .onSuccess { userAlarmLog, _ ->
                    _uiState.update { it.copy(isLoading = false, userAlarmLog = userAlarmLog) }
                }.onError { message, _ ->
                    _uiState.update { it.copy(isLoading = false, error = message) }
                }.onException { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    data class UIState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val userAlarmLog: List<UserAlarmLogResponse> = emptyList()
    )
}