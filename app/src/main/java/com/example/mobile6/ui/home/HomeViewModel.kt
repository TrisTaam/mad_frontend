package com.example.mobile6.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

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

    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val isDoctorMode: Boolean = false
    )
}