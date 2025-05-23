package com.example.mobile6.presentation.ui.prescriptions

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile6.R
import com.example.mobile6.data.remote.dto.response.PrescriptionDetailResponse
import com.example.mobile6.databinding.FragmentPrescriptionCreateBinding
import com.example.mobile6.databinding.ItemPrescriptionDetailMenuBinding
import com.example.mobile6.presentation.ui.MainActivity
import com.example.mobile6.presentation.ui.adapter.PrescriptionDetailAdapter
import com.example.mobile6.presentation.ui.base.BaseFragment
import com.example.mobile6.presentation.ui.util.DateUtils.toRequestDateTimeString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class PrescriptionCreateFragment : BaseFragment<FragmentPrescriptionCreateBinding>() {
    private val viewModel: PrescriptionCreateViewModel by viewModels()
    private lateinit var medicineAdapter: PrescriptionDetailAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentPrescriptionCreateBinding
        get() = { inflater, container ->
            FragmentPrescriptionCreateBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        setupToolbar()
        setupDatePicker()
        setupMedicineList()
        setupAddMedicineButton()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.ivBack.setOnClickListener {
            back()
        }

        // Update title based on edit mode
        viewModel.isEditMode.observe(viewLifecycleOwner) { isEditMode ->
            binding.tvTitle.text = if (isEditMode) "Chỉnh sửa đơn thuốc" else "Tạo đơn thuốc"
        }
    }

    private fun setupDatePicker() {
        binding.cvPrescriptionDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            // If date is already set, use it
            viewModel.prescriptionDate.value?.let {
                calendar.timeInMillis = it.time
            }

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    viewModel.setPrescriptionDate(calendar.time.toRequestDateTimeString())
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }

        // Observe date changes
        viewModel.prescriptionDate.observe(viewLifecycleOwner) { timestamp ->
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.tvPrescriptionDate.text = dateFormat.format(Date(timestamp.time))
            binding.tvPrescriptionDate.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.black
                )
            )
        }
    }

    private fun setupMedicineList() {
        medicineAdapter = PrescriptionDetailAdapter(
            onMoreClick = { medicine, view ->
                showContextMenu(medicine, view)
            }
        )

        binding.rvMedicines.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = medicineAdapter
        }

        // Observe medicine list changes
        viewModel.prescriptionDetail.observe(viewLifecycleOwner) { medicines ->
            medicineAdapter.submitList(medicines)
        }
    }

    private fun showContextMenu(prescriptionDetail: PrescriptionDetailResponse, anchorView: View) {
        val popupWindow = PopupWindow(requireContext())
        val menuBinding =
            ItemPrescriptionDetailMenuBinding.inflate(LayoutInflater.from(requireContext()))

        popupWindow.contentView = menuBinding.root
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.elevation = resources.getDimension(R.dimen.elevation_4dp)
        popupWindow.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        popupWindow.isFocusable = true

        // Set click listeners using binding
        menuBinding.tvEdit.setOnClickListener {
            navigateToEditPrescriptionDetail(prescriptionDetail)
            popupWindow.dismiss()
        }

        menuBinding.tvRemove.setOnClickListener {
            viewModel.removePrescriptionDetail(prescriptionDetail)
            popupWindow.dismiss()
        }

        // Show popup
        popupWindow.showAsDropDown(anchorView, -200, -50)
    }

    private fun setupAddMedicineButton() {
        parentFragmentManager.setFragmentResultListener(
            "savePrescriptionDetailResult",
            viewLifecycleOwner
        ) { _, bundle ->
            val detail = bundle.getSerializable("prescriptionDetail") as PrescriptionDetailResponse
            if (viewModel.isEditMode.value == true) {
                viewModel.updatePrescriptionDetail(detail)
            } else {
                viewModel.addPrescriptionDetail(detail)
            }
        }

        binding.fabAddMedicine.setOnClickListener {
            navigateToAddMedicine()
        }
    }

    private fun sendUsedMedicineIds() {
        val usedMedicineIds: ArrayList<String> =
            viewModel.prescriptionDetail.value?.map { it.medicineId.toString() }
                ?.let { ArrayList(it) } ?: arrayListOf()
        val bundle = Bundle().apply {
            putStringArrayList("usedMedicineIds", usedMedicineIds)
        }
        parentFragmentManager.setFragmentResult("prescriptionData", bundle)
    }

    private fun navigateToAddMedicine() {
        sendUsedMedicineIds()
        navigateTo(R.id.action_prescriptionCreateFragment_to_prescriptionDetailEditFragment)
    }

    private fun navigateToEditPrescriptionDetail(prescriptionDetail: PrescriptionDetailResponse) {
        sendUsedMedicineIds()
        val bundle = Bundle().apply {
            putSerializable("prescriptionDetail", prescriptionDetail)
        }
        navigateTo(R.id.action_prescriptionCreateFragment_to_prescriptionDetailEditFragment, bundle)
    }

    private fun setupSaveButton() {
        binding.btnSavePrescription.setOnClickListener {
            val name = binding.etPrescriptionName.text.toString()
            val notes = binding.etNotes.text.toString()

            viewModel.savePrescription(name, notes)
        }
    }

    override fun initObservers() {
        // Observe UI messages
        viewModel.uiMessage.onEach { message ->
            (requireActivity() as MainActivity).showToast(message)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        // Observe save success
        viewModel.saveSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                val bundle = Bundle().apply {
                    putLong("prescriptionId", viewModel.prescriptionId!!)
                }
                navigateTo(R.id.action_prescriptionCreateFragment_to_prescriptionQrFragment, bundle)
                viewModel.resetSaveSuccess()
            }
        }

        // Observe prescription data for edit mode
        viewModel.prescription.observe(viewLifecycleOwner) { prescription ->
            prescription?.let {
                binding.etPrescriptionName.setText(it.prescriptionName)
                binding.etNotes.setText(it.notes)
                viewModel.setPrescriptionDate(it.prescriptionDate)
            }
        }
    }
}