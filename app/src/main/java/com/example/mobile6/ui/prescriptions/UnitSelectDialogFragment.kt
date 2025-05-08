package com.example.mobile6.ui.prescriptions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mobile6.databinding.FragmentMedicineUnitSelectDialogBinding
import com.example.mobile6.ui.base.BaseDialog

class UnitSelectDialogFragment : BaseDialog<FragmentMedicineUnitSelectDialogBinding>() {
    private val viewModel: PrescriptionDetailEditViewModel by viewModels(
        ownerProducer = { requireParentFragment().childFragmentManager.fragments.first() }
    )

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentMedicineUnitSelectDialogBinding
        get() = { layoutInflater, viewGroup ->
            FragmentMedicineUnitSelectDialogBinding.inflate(layoutInflater, viewGroup, false)
        }

    override fun initViews() {
        binding.cvUnitTablet.setOnClickListener {
            viewModel.setSelectedUnit("Viên")
            dismiss()
        }

        binding.cvUnitSachet.setOnClickListener {
            viewModel.setSelectedUnit("Gói")
            dismiss()
        }

        binding.cvUnitBlister.setOnClickListener {
            viewModel.setSelectedUnit("Vỉ")
            dismiss()
        }

        binding.cvUnitBox.setOnClickListener {
            viewModel.setSelectedUnit("Hộp")
            dismiss()
        }
    }

    override fun initObservers() {
    }
}