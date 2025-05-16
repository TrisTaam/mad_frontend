package com.example.mobile6.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.dto.response.AppointmentResponse
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.model.User
import com.example.mobile6.domain.repository.AppointmentRepository
import com.example.mobile6.domain.repository.AuthRepository
import com.example.mobile6.domain.repository.UserRepository
import com.example.mobile6.ui.util.DateUtils.toRequestDateTimeString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val appointmentRepository: AppointmentRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getUserDetail()
        getAppointmentsWeek()
    }

    private fun getUserDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val mode = authRepository.isDoctorMode()
            _uiState.update { it.copy(isLoading = false, isDoctorMode = mode) }
            val result = userRepository.getDetailInfo()
            result
                .onSuccess { user, message ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = user
                        )
                    }
                }.onError { message, code ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = message
                        )
                    }
                }.onException { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message
                        )
                    }
                }
        }
    }

    private fun getAppointmentsWeek() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val today = LocalDateTime.now()
            val startOfWeek =
                today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN)
            val endOfWeek =
                today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX)

            val startDate = startOfWeek.toRequestDateTimeString()
            val endDate = endOfWeek.toRequestDateTimeString()

            val result = if (uiState.value.isDoctorMode) {
                appointmentRepository.getAppointmentsByDoctorWeek(startDate, endDate)
            } else {
                appointmentRepository.getAppointmentsByUserWeek(startDate, endDate)
            }
            result
                .onSuccess { appointments, message ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            appointments = appointments
                        )
                    }
                }.onError { message, code ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = message
                        )
                    }
                }.onException { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message
                        )
                    }
                }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val user: User = User(),
        val appointments: List<AppointmentResponse> = emptyList(),
        val isDoctorMode: Boolean = false
    )
}