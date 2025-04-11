package com.example.mobile6.ui.medicine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentMedicineListBinding
import com.example.mobile6.ui.base.BaseFragment
import com.example.mobile6.ui.util.gone
import com.example.mobile6.ui.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MedicineListFragment : BaseFragment<FragmentMedicineListBinding>() {
    private val viewModel: MedicineListViewModel by viewModels()
    private lateinit var medicineAdapter: MedicineAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentMedicineListBinding
        get() = { inflater, container ->
            FragmentMedicineListBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        setupRecyclerView()
        setupListeners()
    }

    override fun initObservers() {
        observeUiState()
    }

    private fun setupRecyclerView() {
        medicineAdapter = MedicineAdapter { medicine ->
            try {
                // Xử lý khi nhấn vào một thuốc - chuyển sang màn hình chi tiết
                val bundle = Bundle().apply {
                    putLong("medicineId", medicine.id)
                }
                findNavController().navigate(
                    R.id.medicineDetailFragment,
                    bundle
                )
            } catch (e: Exception) {
                // Tạm thời bỏ qua lỗi navigation
                Toast.makeText(context, "Chi tiết thuốc đang được phát triển", Toast.LENGTH_SHORT).show()
            }
        }
        binding.rvMedicines.adapter = medicineAdapter
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        
        binding.etSearch.doAfterTextChanged { text ->
            viewModel.searchMedicines(text.toString())
        }
    }

    private fun observeUiState() {
        viewModel.uiState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { uiState ->
                when {
                    uiState.isLoading -> {
                        binding.progressBar.visible()
                        binding.rvMedicines.gone()
                        binding.tvError.gone()
                    }
                    uiState.error != null -> {
                        binding.progressBar.gone()
                        binding.rvMedicines.gone()
                        binding.tvError.visible()
                        binding.tvError.text = uiState.error
                    }
                    uiState.medicines.isEmpty() -> {
                        binding.progressBar.gone()
                        binding.rvMedicines.gone()
                        binding.tvError.visible()
                        binding.tvError.text = "Không tìm thấy thuốc nào"
                    }
                    else -> {
                        binding.progressBar.gone()
                        binding.rvMedicines.visible()
                        binding.tvError.gone()
                        medicineAdapter.submitList(uiState.medicines)
                    }
                }
            }
            .launchIn(lifecycleScope)
    }
} 