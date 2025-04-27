package com.example.mobile6.ui.medicine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile6.domain.model.Medicine
import com.example.mobile6.domain.model.Resource
import com.example.mobile6.domain.repository.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineDetailViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MedicineDetailUiState());
    val uiState: StateFlow<MedicineDetailUiState> = _uiState.asStateFlow()

    fun fetchMedicineDetail(medicineId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = medicineRepository.getMedicineById(medicineId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            medicine = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is Resource.Exception->{
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error=result.throwable.message ?: "Lỗi fetchMedicineDetail"
                        )
                    }
                }
            }
        }
    }
}

data class MedicineDetailUiState(
    val medicine: Medicine? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)