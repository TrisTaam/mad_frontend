package com.example.mobile6.presentation.ui.prescriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.dto.request.CreateUserAlarmRequest
import com.example.mobile6.data.remote.dto.response.UserAlarmResponse
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.repository.UserAlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrescriptionDetailAlarmViewModel @Inject constructor(
    private val userAlarmRepository: UserAlarmRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    var prescriptionDetailId: Long = 0
        set(value) = run {
            field = value
            getUserAlarms()
        }

    fun createAlarm(notifyTime: String) {
        viewModelScope.launch {
            val request = CreateUserAlarmRequest(
                prescriptionDetailId = prescriptionDetailId,
                notifyTime = notifyTime
            )

            _uiState.update { it.copy(isLoading = true, success = null) }
            userAlarmRepository.createAlarm(request)
                .onSuccess { _, _ ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            success = "Thêm nhắc nhở thành công"
                        )
                    }
                    getUserAlarms()
                }.onError { message, _ ->
                    _uiState.update { it.copy(isLoading = false, error = message) }
                }
                .onException { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun getUserAlarms() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = false, success = null) }
            userAlarmRepository.getUserAlarms(prescriptionDetailId)
                .onSuccess { userAlarms, _ ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userAlarms = userAlarms.sortedBy { it.notifyTime })
                    }
                }.onError { message, _ ->
                    _uiState.update { it.copy(isLoading = false, error = message) }
                }
                .onException { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun deleteAlarm(alarmId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, success = null) }
            userAlarmRepository.deleteAlarm(alarmId)
                .onSuccess { _, _ ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            success = "Xoá nhắc nhở thành công"
                        )
                    }
                    getUserAlarms()
                }.onError { message, _ ->
                    _uiState.update { it.copy(isLoading = false, error = message) }
                }
                .onException { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    data class UIState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val success: String? = null,
        val userAlarms: List<UserAlarmResponse> = emptyList(),
    )
}