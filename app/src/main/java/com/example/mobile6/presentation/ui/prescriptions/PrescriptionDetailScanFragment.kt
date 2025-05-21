package com.example.mobile6.presentation.ui.prescriptions

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile6.R
import com.example.mobile6.data.remote.dto.response.PrescriptionResponse
import com.example.mobile6.databinding.FragmentPrescriptionDetailScanBinding
import com.example.mobile6.databinding.ItemPrescriptionDetailMenuAlarmBinding
import com.example.mobile6.presentation.ui.MainActivity
import com.example.mobile6.presentation.ui.adapter.PrescriptionDetailAdapter
import com.example.mobile6.presentation.ui.base.BaseFragment
import com.example.mobile6.presentation.ui.util.DateUtils.toUtilDate
import com.example.mobile6.presentation.ui.util.gone
import com.example.mobile6.presentation.ui.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class PrescriptionDetailScanFragment : BaseFragment<FragmentPrescriptionDetailScanBinding>() {
    private val viewModel: PrescriptionDetailScanViewModel by viewModels()
    private lateinit var prescriptionDetailAdapter: PrescriptionDetailAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentPrescriptionDetailScanBinding
        get() = { inflater, container ->
            FragmentPrescriptionDetailScanBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        binding.ivBack.setOnClickListener {
            back()
        }
        binding.btnSavePrescription.setOnClickListener {
            viewModel.savePrescription()
        }
        binding.btnCompletePrescription.setOnClickListener {

        }

        setupMedicineList()

        // Get prescription ID from arguments
        arguments?.getLong("prescriptionId")?.let { id ->
            viewModel.loadPrescription(id)
        }

        val isView = arguments?.getBoolean("isView", false) == true
        if (isView) {
            binding.btnSavePrescription.gone()
            binding.btnCompletePrescription.visible()
            prescriptionDetailAdapter.onMoreClick = { prescriptionDetail, view ->
                val popupWindow = PopupWindow(requireContext())
                val menuBinding =
                    ItemPrescriptionDetailMenuAlarmBinding.inflate(
                        LayoutInflater.from(
                            requireContext()
                        )
                    )

                popupWindow.contentView = menuBinding.root
                popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
                popupWindow.elevation = resources.getDimension(R.dimen.elevation_4dp)
                popupWindow.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                popupWindow.isFocusable = true

                menuBinding.tvEditAlarm.setOnClickListener {
                    val bundle = bundleOf("prescriptionDetailId" to prescriptionDetail.id)
                    navigateTo(
                        R.id.action_prescriptionDetailScanFragment_to_prescriptionDetailAlarmSheetFragment,
                        bundle
                    )
                    popupWindow.dismiss()
                }

                // Show popup
                popupWindow.showAsDropDown(view, -200, -50)
            }
        } else {
            binding.btnSavePrescription.visible()
            binding.btnCompletePrescription.gone()
            binding.btnCompletePrescription.setOnClickListener {
                viewModel.donePrescription()
            }
            prescriptionDetailAdapter.onMoreClick = { prescriptionDetail, view ->
            }
        }
    }

    private fun setupMedicineList() {
        prescriptionDetailAdapter = PrescriptionDetailAdapter()

        binding.rvMedicines.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = prescriptionDetailAdapter
        }
    }

    override fun initObservers() {
        viewModel.uiMessage.onEach { message ->
            (requireActivity() as MainActivity).showToast(message)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.prescription.observe(viewLifecycleOwner) { prescription ->
            prescription?.let {
                updateUI(it)
            }
        }

        viewModel.saveResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(
                    requireContext(),
                    "Đã lưu đơn thuốc thành công",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.doneResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(
                    requireContext(),
                    "Đã hoàn thành đơn thuốc thành công",
                    Toast.LENGTH_SHORT
                ).show()
                binding.btnCompletePrescription.text = "Đã hoàn thành"
                binding.btnCompletePrescription.isEnabled = false
            }
        }
    }

    private fun updateUI(prescription: PrescriptionResponse) {
        binding.tvPrescriptionName.text = prescription.prescriptionName
        binding.tvDoctorName.text = "Bác sĩ: ${prescription.doctorName}"

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.tvPrescriptionDate.text =
            "Ngày kê đơn: ${dateFormat.format(prescription.prescriptionDate.toUtilDate())}"

        binding.tvNotes.text = prescription.notes

        prescriptionDetailAdapter.submitList(prescription.details)
    }
}