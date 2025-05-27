package com.example.mobile6.presentation.ui.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.dto.response.AppointmentResponse
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.repository.AppointmentRepository
import com.example.mobile6.domain.repository.AuthRepository
import com.example.mobile6.presentation.ui.util.DateUtils.toLocalDate
import com.example.mobile6.presentation.ui.util.DateUtils.toUtilDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AppointmentsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appointmentRepository: AppointmentRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    val appointmentsGroupDate = mutableMapOf<LocalDate, List<AppointmentResponse>>()

    init {
//        getAppointments()
    }

    fun getAppointments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, success = null) }
            val mode = authRepository.isDoctorMode()
            _uiState.update { it.copy(isDoctorMode = mode) }

            val result = if (mode) {
                appointmentRepository.getAppointmentsByDoctor()
            } else {
                appointmentRepository.getAppointmentsByUser()
            }

            result
                .onSuccess { appointments, message ->
                    _uiState.update {
                        appointmentsGroupDate.clear()
                        appointments.groupBy { it.appointmentDate.toUtilDate().toLocalDate() }
                            .forEach { (date, appointments) ->
                                appointmentsGroupDate[date] =
                                    appointments.sortedBy { it.appointmentDate }
                            }
                        it.copy(isLoading = false)
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

    fun approveAppointment(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, success = null) }
            appointmentRepository.approveAppointment(id)
                .onSuccess { _, message ->
                    _uiState.update {
                        it.copy(isLoading = false, success = message)
                    }
                    getAppointments()
                }.onError { message, code ->
                    _uiState.update {
                        it.copy(isLoading = false, error = message)
                    }
                }.onException { throwable ->
                    _uiState.update {
                        it.copy(isLoading = false, error = throwable.message)
                    }
                }
        }
    }

    fun cancelAppointment(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, success = null) }
            appointmentRepository.cancelAppointment(id)
                .onSuccess { _, message ->
                    _uiState.update {
                        it.copy(isLoading = false, success = message)
                    }
                    getAppointments()
                }.onError { message, code ->
                    _uiState.update {
                        it.copy(isLoading = false, error = message)
                    }
                }.onException { throwable ->
                    _uiState.update {
                        it.copy(isLoading = false, error = throwable.message)
                    }
                }
        }
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { it.copy(appointments = appointmentsGroupDate[date] ?: emptyList()) }
    }

    data class UIState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val success: String? = null,
        val isDoctorMode: Boolean = false,
        val appointments: List<AppointmentResponse> = emptyList(),
    )
}