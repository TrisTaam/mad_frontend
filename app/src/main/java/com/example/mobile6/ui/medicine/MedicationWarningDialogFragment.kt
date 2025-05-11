package com.example.mobile6.ui.medicine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mobile6.databinding.FragmentMedicationWarningDialogBinding
import com.example.mobile6.ui.base.BaseDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicationWarningDialogFragment : BaseDialog<FragmentMedicationWarningDialogBinding>() {

    private var medicineId: Long = 0L
    private var message: String = ""
    private val viewModel: MedicineDetailViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentMedicationWarningDialogBinding
        get() = { inflater, container ->
            FragmentMedicationWarningDialogBinding.inflate(inflater, container, false)
        }

    override fun processArguments(args: Bundle) {
        medicineId = arguments?.getLong("medicineId") ?: 0L
        message = arguments?.getString("interactionMessage") ?: ""
    }

    override fun initViews() {
        binding.tvWarningMessage.text = message

        binding.btnWarningReview.setOnClickListener {
            dismiss()
        }

        binding.btnWarningContinue.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("selectedMedicine", viewModel.uiState.value.medicine)
            }
            parentFragmentManager.setFragmentResult("prescriptionDataSelectMedicine", bundle)
            dismiss()
            findNavController().navigateUp()
            findNavController().navigateUp()
        }
    }

    override fun initObservers() {
    }
}