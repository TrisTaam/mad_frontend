package com.example.mobile6.presentation.ui.doctor.doctor_specialty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentDoctorSpecialtyBinding
import com.example.mobile6.presentation.ui.adapter.DoctorSpecialtyAdapter
import com.example.mobile6.presentation.ui.base.BaseFragment
import com.example.mobile6.presentation.ui.util.defaultAnim
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DoctorSpecialtyFragment : BaseFragment<FragmentDoctorSpecialtyBinding>() {

    private val viewModel: DoctorSpecialtyViewModel by viewModels()
    private lateinit var doctorSpecialtyAdapter: DoctorSpecialtyAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentDoctorSpecialtyBinding
        get() = { inflater, container ->
            FragmentDoctorSpecialtyBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        binding.backButton.setOnClickListener {
            back()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        doctorSpecialtyAdapter = DoctorSpecialtyAdapter { doctor ->
            val bundle = Bundle().apply {
                putString("specialty", doctor.specialty)
            }
            navigateTo(
                R.id.action_doctorSpecialtyFragment_to_doctorListFragment,
                bundle,
                NavOptions.Builder().defaultAnim().build()
            )
        }

        binding.doctorsRecyclerView.apply {
            adapter = doctorSpecialtyAdapter
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
                    val uniqueSpecialties = state.doctors
                        .groupBy { it.specialty }
                        .map { it.value.first() }
                    doctorSpecialtyAdapter.submitList(uniqueSpecialties)
                }
            }
        }
    }

    private fun handleLoadingState(isLoading: Boolean) {
    }

    private fun handleErrorState(error: String?) {
    }

}