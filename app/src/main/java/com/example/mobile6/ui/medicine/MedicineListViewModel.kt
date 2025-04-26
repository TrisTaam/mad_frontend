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
class MedicineListViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MedicineListUiState())
    val uiState: StateFlow<MedicineListUiState> = _uiState.asStateFlow()

    private var allMedicines = emptyList<Medicine>()

    init {
        fetchMedicines()
    }

    fun fetchMedicines() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = medicineRepository.getMedicines()) {
                is Resource.Success -> {
                    allMedicines = result.data
                    _uiState.update {
                        it.copy(
                            medicines = result.data,
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
                is Resource.Exception -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.throwable.message ?: "Đã xảy ra lỗi không mong muốn"
                        )
                    }
                }
            }
        }
    }

    fun searchMedicines(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(medicines = allMedicines) }
            return
        }
        
        val filteredList = allMedicines.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.ingredients.any { ingredient -> 
                ingredient.name.contains(query, ignoreCase = true)
            }
        }
        
        _uiState.update { it.copy(medicines = filteredList) }
    }
}

data class MedicineListUiState(
    val medicines: List<Medicine> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 