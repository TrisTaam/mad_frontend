package com.example.mobile6.ui.home

import androidx.lifecycle.ViewModel
import com.example.mobile6.domain.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val testRepository: TestRepository
) : ViewModel() {

}