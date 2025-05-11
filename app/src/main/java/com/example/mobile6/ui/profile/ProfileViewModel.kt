package com.example.mobile6.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onException
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.model.User
import com.example.mobile6.domain.repository.AuthRepository
import com.example.mobile6.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    init {
        getUserDetail()
    }

    private fun getUserDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            var isDoctorMode: Boolean? = null
            if (authRepository.isDoctorSignedIn()) {
                Timber.d("Doctor signed in")
                isDoctorMode = authRepository.isDoctorMode()
            }
            val result = userRepository.getDetailInfo()
            result
                .onSuccess { user, message ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = user,
                            isDoctorMode = isDoctorMode
                        )
                    }
                }.onError { message, code ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = message,
                            isDoctorMode = isDoctorMode
                        )
                    }
                }.onException { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message,
                            isDoctorMode = isDoctorMode
                        )
                    }
                }
        }
    }

    fun changeMode() {
        viewModelScope.launch {
            _uiState.value.isDoctorMode?.let { isDoctorMode ->
                authRepository.changeDoctorMode(!isDoctorMode)
            } ?: run {
                return@launch
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _uiState.update { UIState(isLoading = true) }
            val result = authRepository.signOut()

            result
                .onSuccess { unit, message ->
                    _uiState.update { it.copy(isLoading = false) }
                }.onError { message, code ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                }.onException { throwable ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = throwable.message) }
                }
        }
    }

    data class UIState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val user: User = User(),
        val isDoctorMode: Boolean? = null
    )
}