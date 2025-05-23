package com.example.mobile6.presentation.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.data.remote.dto.request.UpdateAdditionalUserInfoRequest
import com.example.mobile6.data.remote.util.onError
import com.example.mobile6.data.remote.util.onSuccess
import com.example.mobile6.domain.repository.UserRepository
import com.example.mobile6.presentation.ui.util.DateUtils.toRequestDateString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ProfileCompletionViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage = _uiMessage.asSharedFlow()

    private val _gender = MutableLiveData<String>()
    val gender: LiveData<String> = _gender

    private val _dateOfBirth = MutableLiveData<Date>()
    val dateOfBirth: LiveData<Date> = _dateOfBirth

    private val _weightError = MutableLiveData<String?>()
    val weightError: LiveData<String?> = _weightError

    private val _heightError = MutableLiveData<String?>()
    val heightError: LiveData<String?> = _heightError

    private val _navigateToNextScreen = MutableLiveData<Boolean>()
    val navigateToNextScreen: LiveData<Boolean> = _navigateToNextScreen

    fun setGender(gender: String) {
        _gender.value = gender
    }

    fun setDateOfBirth(timestamp: Date) {
        _dateOfBirth.value = timestamp
    }

    fun validateAndSaveProfile(weightStr: String, heightStr: String) {
        // Reset errors
        _weightError.value = null
        _heightError.value = null

        var isValid = true

        // Validate gender
        if (_gender.value.isNullOrEmpty()) {
            viewModelScope.launch {
                _uiMessage.emit("Vui lòng chọn giới tính")
            }
            isValid = false
        }

        // Validate date of birth
        if (_dateOfBirth.value == null || _dateOfBirth.value?.time == 0L) {
            viewModelScope.launch {
                _uiMessage.emit("Vui lòng chọn ngày sinh")
            }
            isValid = false
        }

        // Validate weight
        if (weightStr.isEmpty()) {
            _weightError.value = "Vui lòng nhập cân nặng"
            isValid = false
        } else {
            try {
                val weight = weightStr.toFloat()
                if (weight <= 0 || weight > 300) {
                    _weightError.value = "Cân nặng không hợp lệ"
                    isValid = false
                }
            } catch (e: NumberFormatException) {
                _weightError.value = "Cân nặng không hợp lệ"
                isValid = false
            }
        }

        // Validate height
        if (heightStr.isEmpty()) {
            _heightError.value = "Vui lòng nhập chiều cao"
            isValid = false
        } else {
            try {
                val height = heightStr.toInt()
                if (height <= 0 || height > 250) {
                    _heightError.value = "Chiều cao không hợp lệ"
                    isValid = false
                }
            } catch (e: NumberFormatException) {
                _heightError.value = "Chiều cao không hợp lệ"
                isValid = false
            }
        }

        // If all validations pass, save the profile
        if (isValid) {
            saveProfile(weightStr.toInt(), heightStr.toInt())
        }
    }

    private fun saveProfile(weight: Int, height: Int) {
        viewModelScope.launch {
            try {
                val userProfile = UpdateAdditionalUserInfoRequest(
                    gender = _gender.value ?: "",
                    dateOfBirth = _dateOfBirth.value?.toRequestDateString() ?: "",
                    weight = weight,
                    height = height
                )

                userRepository.updateAdditionalInformation(userProfile)
                    .onSuccess { data, message ->
                        _uiMessage.emit("Hồ sơ đã được lưu thành công")
                    }
                    .onError { message, code ->
                        _uiMessage.emit("Lưu không thành công")
                    }

                _navigateToNextScreen.value = true
            } catch (e: Exception) {
                Timber.e(e)
                _uiMessage.emit("Lỗi: ${e.message}")
            }
        }
    }

    fun onNavigationComplete() {
        _navigateToNextScreen.value = false
    }
}