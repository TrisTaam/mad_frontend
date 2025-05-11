package com.example.mobile6.ui.appointments

import androidx.lifecycle.ViewModel
import com.example.mobile6.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppointmentsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

}