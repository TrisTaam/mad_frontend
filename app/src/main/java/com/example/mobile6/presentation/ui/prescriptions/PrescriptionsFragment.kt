package com.example.mobile6.presentation.ui.prescriptions

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentPrescriptionsBinding
import com.example.mobile6.presentation.ui.MainActivity
import com.example.mobile6.presentation.ui.adapter.PrescriptionAdapter
import com.example.mobile6.presentation.ui.base.BaseFragment
import com.example.mobile6.presentation.ui.util.gone
import com.example.mobile6.presentation.ui.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PrescriptionsFragment : BaseFragment<FragmentPrescriptionsBinding>() {
    private val viewModel: PrescriptionsViewModel by viewModels()

    private lateinit var adapter: PrescriptionAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentPrescriptionsBinding
        get() = { inflater, container ->
            FragmentPrescriptionsBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        binding.backButton.setOnClickListener {
            back()
        }

        adapter = PrescriptionAdapter()
        binding.rvPrescriptions.adapter = adapter

        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.search(text.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPrescriptions()
    }

    override fun initObservers() {
        viewModel.uiState.onEach { uiState ->
            if (uiState.isLoading) {
                binding.fabAddPrescription.gone()
                return@onEach
            }
            if (uiState.errorMessage != null) {
                (requireActivity() as MainActivity).showToast(uiState.errorMessage)
                binding.fabAddPrescription.gone()
                return@onEach
            }
            handleMode(uiState.isDoctorMode)
            binding.fabAddPrescription.visible()

            adapter.isDoctorMode = uiState.isDoctorMode
            adapter.submitList(uiState.prescriptions)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleMode(isDoctorMode: Boolean) {
        binding.fabAddPrescription.setOnClickListener {
            navigateTo(
                if (isDoctorMode) {
                    R.id.action_prescriptionsFragment_to_prescriptionCreateFragment
                } else {
                    R.id.action_prescriptionsFragment_to_prescriptionScanFragment
                }
            )
        }
        adapter.onPrescriptionClick = { prescription ->
            if (isDoctorMode) {
                val bundle = Bundle().apply {
                    putLong("prescriptionId", prescription.prescriptionId ?: 0)
                }
                navigateTo(R.id.action_prescriptionsFragment_to_prescriptionCreateFragment, bundle)
            } else {
                val bundle = Bundle().apply {
                    putLong("prescriptionId", prescription.prescriptionId ?: 0)
                    putBoolean("isView", true)
                }
                navigateTo(
                    R.id.action_prescriptionsFragment_to_prescriptionDetailScanFragment,
                    bundle
                )
            }
        }
        Glide.with(requireContext())
            .load(
                if (isDoctorMode) {
                    R.drawable.ic_add
                } else {
                    R.drawable.ic_scan
                }
            )
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    drawable: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    binding.fabAddPrescription.setImageDrawable(drawable)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}