package com.example.mobile6.ui.medicine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentMedicineSearchBinding
import com.example.mobile6.ui.adapter.MedicineAdapter
import com.example.mobile6.ui.base.BaseFragment
import com.example.mobile6.ui.util.defaultAnim
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MedicineSearchFragment : BaseFragment<FragmentMedicineSearchBinding>() {

    private val viewModel: MedicineListViewModel by viewModels()
    private lateinit var medicineAdapter: MedicineAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentMedicineSearchBinding
        get() = { inflater, container ->
            FragmentMedicineSearchBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        binding.backButton.setOnClickListener {
            back()
        }

        setupRecyclerView()
        setupSearchView()
    }

    private fun setupRecyclerView() {
        medicineAdapter = MedicineAdapter { medicine ->
            val bundle = Bundle().apply {
                putLong("medicineId", medicine.id);
            }
            navigateTo(
                R.id.action_medicineSearchFragment_to_medicineDetailFragment,
                bundle
            )
        }

        binding.medicinesRecyclerView.apply {
            adapter = medicineAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSearchView() {
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.searchMedicines(text.toString())
        }
    }

    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    if (state.isLoading) {
                        handleLoadingState(true)
                        return@collectLatest
                    }
                    if (state.error != null) {
                        handleErrorState(state.error)
                        return@collectLatest
                    }
                    medicineAdapter.submitList(state.medicines)
                }
            }
        }
    }

    private fun handleLoadingState(isLoading: Boolean) {
        // Show/hide loading indicator if you have one
        // binding.progressBar.isVisible = isLoading
    }

    private fun handleErrorState(error: String?) {
        // Show error message if needed
        // error?.let {
        //     Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        // }
    }
}