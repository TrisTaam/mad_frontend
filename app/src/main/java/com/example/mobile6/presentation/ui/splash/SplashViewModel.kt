package com.example.mobile6.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _isRefreshSuccess = MutableStateFlow<Boolean?>(null)
    val isRefreshSuccess = _isRefreshSuccess.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.refreshToken().let { result ->
                _isRefreshSuccess.update { result is Resource.Success }
            }
        }
    }
}