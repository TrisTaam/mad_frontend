package com.example.mobile6.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.dto.request.CreateUserAlarmLogRequest
import com.example.mobile6.data.remote.dto.response.UserAlarmResponse
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.repository.AuthRepository
import com.example.mobile6.domain.repository.UserAlarmLogRepository
import com.example.mobile6.domain.repository.UserAlarmRepository
import com.example.mobile6.presentation.ui.util.DateUtils.toRequestDateTimeString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userAlarmRepository: UserAlarmRepository,
    private val userAlarmLogRepository: UserAlarmLogRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    var userAlarmId: Long = 0
        set(value) = run {
            field = value
            getUserAlarm()
        }

    init {
        viewModelScope.launch {
            authRepository.isLoggedIn().collect { isLoggedIn ->
                _uiState.update { it.copy(isLoggedIn = isLoggedIn) }
            }
        }
    }

    fun getUserAlarm() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            userAlarmRepository.getUserAlarm(userAlarmId)
                .onSuccess { userAlarm, _ ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userAlarm = userAlarm
                        )
                    }
                }.onError { message, _ ->
                    _uiState.update { it.copy(isLoading = false, error = message) }
                }
                .onException { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun createUserAlarmLog(notifyAt: Calendar, confirmAt: Calendar) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val request = CreateUserAlarmLogRequest(
                userAlarmId = userAlarmId,
                notifyAt = notifyAt.time.toRequestDateTimeString(),
                confirmAt = confirmAt.time.toRequestDateTimeString()
            )
            userAlarmLogRepository.createUserAlarmLog(request)
                .onSuccess { userAlarmLog, _ ->
                    _uiState.update { it.copy(isLoading = false) }
                }.onError { message, _ ->
                    _uiState.update { it.copy(isLoading = false, error = message) }
                }
                .onException { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    data class UIState(
        val isLoggedIn: Boolean? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val userAlarm: UserAlarmResponse = UserAlarmResponse(0, 0, 0, "", false, 0, "", "")
    )
}