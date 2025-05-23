package com.example.mobile6.presentation.ui.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
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
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val application: Application,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    init {
        getUserDetail()
    }

    fun getUserDetail() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    successMessage = null
                )
            }
            var isDoctorMode: Boolean? = null
            if (authRepository.isDoctorSignedIn()) {
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

    fun updateAvatar(uri: Uri) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null,
                        successMessage = null
                    )
                }

                val file = createFileFromUri(uri)

                val result = userRepository.updateAvatar(file)

                result.onSuccess { _, message ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = message
                        )
                    }
                    getUserDetail()
                }.onError { message, _ ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = message
                        )
                    }
                }.onException { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message
                                ?: "Có lỗi xảy ra khi cập nhật ảnh đại diện"
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error updating avatar")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Có lỗi xảy ra khi xử lý ảnh"
                    )
                }
            }
        }
    }

    private fun createFileFromUri(uri: Uri): File {
        val inputStream = application.contentResolver.openInputStream(uri)
        val tempFile = File(application.cacheDir, "temp_avatar_${System.currentTimeMillis()}.jpg")

        FileOutputStream(tempFile).use { outputStream ->
            inputStream?.copyTo(outputStream)
        }

        inputStream?.close()
        return tempFile
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
            _uiState.update { UIState(isLoading = true, successMessage = null) }
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
        val successMessage: String? = null,
        val user: User = User(),
        val isDoctorMode: Boolean? = null
    )
}