package com.example.mobile6.ui.prescriptions

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentPrescriptionDetailEditBinding
import com.example.mobile6.domain.model.Medicine
import com.example.mobile6.ui.MainActivity
import com.example.mobile6.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PrescriptionDetailEditFragment : BaseFragment<FragmentPrescriptionDetailEditBinding>() {
    private val viewModel: PrescriptionDetailEditViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentPrescriptionDetailEditBinding
        get() = { inflater, container ->
            FragmentPrescriptionDetailEditBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        setupToolbar()
        setupMedicineSelection()
        setupUnitSelection()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.ivBack.setOnClickListener {
            back()
        }

        // Update title based on edit mode
        viewModel.isEditMode.observe(viewLifecycleOwner) { isEditMode ->
            binding.tvTitle.text = if (isEditMode) "Chỉnh sửa thuốc" else "Thêm thuốc"
        }
    }

    private fun setupMedicineSelection() {
        parentFragmentManager.setFragmentResultListener("prescriptionDataSelectMedicine", viewLifecycleOwner) { _, bundle ->
            val selectedMedicine = bundle.getSerializable("selectedMedicine")
            viewModel.setSelectedMedicine(selectedMedicine as Medicine)
        }

        binding.cvMedicineSelection.setOnClickListener {
            // Navigate to medicine selection screen
            navigateTo(R.id.action_prescriptionDetailEditFragment_to_medicineSearchFragment)
        }

        // Observe selected medicine
        viewModel.selectedMedicine.observe(viewLifecycleOwner) { medicine ->
            medicine?.let {
                binding.tvSelectedMedicine.text = it.name
                binding.tvSelectedMedicine.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))

                // Update medicine info
                updatePrescriptionDetailInfo(it)
            }
        }
    }

    private fun updatePrescriptionDetailInfo(medicine: Medicine) {
        // Update usage info
        binding.tvUsageInfo.text = medicine.usage.trim()

        // Update composition info
        val compositionBuilder = StringBuilder()
        if (medicine.ingredients.isNotEmpty()) {
            for (ingredient in medicine.ingredients) {
                compositionBuilder.append("• ${ingredient.name} - ${ingredient.amount}\n")
            }
        }
        binding.tvCompositionInfo.text = compositionBuilder.toString().trim()
    }

    private fun setupUnitSelection() {
        binding.tvUnit.setOnClickListener {
            showUnitSelectionDialog()
        }

        // Observe selected unit
        viewModel.selectedUnit.observe(viewLifecycleOwner) { unit ->
            binding.tvUnit.text = unit
        }
    }

    private fun showUnitSelectionDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_medicine_unit_select_dialog)

        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
//        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Set click listeners for unit options
        dialog.findViewById<CardView>(R.id.cv_unit_tablet).setOnClickListener {
            viewModel.setSelectedUnit("Viên")
            dialog.dismiss()
        }

        dialog.findViewById<CardView>(R.id.cv_unit_sachet).setOnClickListener {
            viewModel.setSelectedUnit("Gói")
            dialog.dismiss()
        }

        dialog.findViewById<CardView>(R.id.cv_unit_blister).setOnClickListener {
            viewModel.setSelectedUnit("Vỉ")
            dialog.dismiss()
        }

        dialog.findViewById<CardView>(R.id.cv_unit_box).setOnClickListener {
            viewModel.setSelectedUnit("Hộp")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val quantity = binding.etQuantity.text.toString()
            val dosage = binding.etDosage.text.toString()

            val result = viewModel.savePrescriptionDetail(quantity, dosage)
            if (result == null) {
                return@setOnClickListener
            }
            val bundle = Bundle().apply {
                putSerializable("prescriptionDetail", result)
            }
            parentFragmentManager.setFragmentResult("savePrescriptionDetailResult", bundle)
            back()
        }
    }

    override fun initObservers() {
        // Observe UI messages
        viewModel.uiMessage.onEach { message ->
            (requireActivity() as MainActivity).showToast(message)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        // Observe medicine data for edit mode
        viewModel.prescriptionDetail.observe(viewLifecycleOwner) { detail ->
            detail?.let {
                binding.etQuantity.setText(it.quantity.toString())
                binding.etDosage.setText(it.quantityUsage)
            }
        }
    }
}