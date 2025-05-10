package com.example.mobile6.ui.prescriptions

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile6.data.remote.dto.response.PrescriptionResponse
import com.example.mobile6.databinding.FragmentPrescriptionDetailScanBinding
import com.example.mobile6.ui.MainActivity
import com.example.mobile6.ui.adapter.PrescriptionDetailAdapter
import com.example.mobile6.ui.base.BaseFragment
import com.example.mobile6.ui.util.DateUtils.toUtilDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Date
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

        setupMedicineList()

        // Get prescription ID from arguments
        arguments?.getLong("prescriptionId")?.let { id ->
            viewModel.loadPrescription(id)
        }
    }

    private fun setupMedicineList() {
        prescriptionDetailAdapter = PrescriptionDetailAdapter(
            onMoreClick = { _, _ ->
                // No actions needed for patient view
            }
        )

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
    }

    private fun updateUI(prescription: PrescriptionResponse) {
        binding.tvPrescriptionName.text = prescription.prescriptionName
        binding.tvDoctorName.text = "Bác sĩ: ${prescription.doctorName}"

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.tvPrescriptionDate.text = "Ngày kê đơn: ${dateFormat.format(prescription.prescriptionDate.toUtilDate())}"

        binding.tvNotes.text = prescription.notes

        prescriptionDetailAdapter.submitList(prescription.details)
    }
}