package com.example.mobile6.presentation.ui.prescriptions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mobile6.databinding.FragmentPrescriptionQrBinding
import com.example.mobile6.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrescriptionQrFragment : BaseFragment<FragmentPrescriptionQrBinding>() {
    private val viewModel: PrescriptionQrViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentPrescriptionQrBinding
        get() = { inflater, container ->
            FragmentPrescriptionQrBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        // Get prescription ID from arguments
        arguments?.getLong("prescriptionId")?.let { id ->
            viewModel.setPrescriptionId(id)
        }
        // Set complete button click listener
        binding.btnComplete.setOnClickListener {
            back()
        }
    }

    override fun initObservers() {
        viewModel.prescriptionId.observe(viewLifecycleOwner) { id ->
            binding.tvPrescriptionId.text = String.format("%09d", id)
        }
        viewModel.qrCode.observe(viewLifecycleOwner) { image ->
            binding.ivQrCode.setImageBitmap(image)
        }
    }
}