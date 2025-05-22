package com.example.mobile6.presentation.ui.doctor.doctor_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentDoctorListBinding
import com.example.mobile6.presentation.ui.adapter.DoctorListAdapter
import com.example.mobile6.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DoctorListFragment : BaseFragment<FragmentDoctorListBinding>() {

    private val viewModel: DoctorListViewModel by viewModels()
    private lateinit var doctorListAdapter: DoctorListAdapter
    private var specialty: String = ""

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentDoctorListBinding
        get() = { inflater, container ->
            FragmentDoctorListBinding.inflate(inflater, container, false)
        }

    override fun processArguments(args: Bundle) {
        specialty = arguments?.getString("specialty") ?: ""
        if (viewModel.uiState.value.doctors.isEmpty()) {
            viewModel.fetchDoctorsBySpecialty(specialty)
        }
    }

    override fun initViews() {
        binding.backButton.setOnClickListener {
            back()
        }

        setupRecyclerView()
        setupSearchView()
    }

    private fun setupRecyclerView() {
        doctorListAdapter = DoctorListAdapter { doctor ->
            val bundle = Bundle().apply {
                putParcelable("doctor", doctor)
            }
            navigateTo(
                R.id.action_doctorListFragment_to_doctorDetailFragment,
                bundle
            )
        }

        binding.doctorsRecyclerView.apply {
            adapter = doctorListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSearchView() {
        viewModel.searchDoctors("Mùi")
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.searchDoctors(text.toString())
        }
        binding.listTitle.text = "Danh sách bác sĩ $specialty"
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
                    doctorListAdapter.submitList(state.doctors)
                }
            }
        }
    }

    private fun handleLoadingState(isLoading: Boolean) {
    }

    private fun handleErrorState(error: String?) {
    }
}