package com.example.mobile6.ui.doctor.doctor_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobile6.databinding.FragmentDoctorListBinding
import com.example.mobile6.ui.base.BaseFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile6.ui.adapter.DoctorListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

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
        viewModel.fetchDoctorsBySpecialty(specialty)
        Timber.d("muinv ${viewModel.uiState}")
    }

    override fun initViews() {
        binding.backButton.setOnClickListener {
            back()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        doctorListAdapter = DoctorListAdapter {  }

        binding.doctorsRecyclerView.apply {
            adapter = doctorListAdapter
            layoutManager = LinearLayoutManager(requireContext())
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
                    doctorListAdapter.submitList(state.doctors)
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