package com.example.mobile6.presentation.ui.prescriptions

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mobile6.databinding.FragmentPrescriptionDetailAlarmSheetBinding
import com.example.mobile6.presentation.ui.MainActivity
import com.example.mobile6.presentation.ui.adapter.UserAlarmAdapter
import com.example.mobile6.presentation.ui.base.BaseBottomSheet
import com.example.mobile6.presentation.ui.util.ReminderScheduler
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalTime
import java.util.Calendar

@AndroidEntryPoint
class PrescriptionDetailAlarmSheetFragment :
    BaseBottomSheet<FragmentPrescriptionDetailAlarmSheetBinding>() {
    private val viewModel: PrescriptionDetailAlarmViewModel by viewModels()
    private lateinit var adapter: UserAlarmAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentPrescriptionDetailAlarmSheetBinding
        get() = { inflater, container ->
            FragmentPrescriptionDetailAlarmSheetBinding.inflate(inflater, container, false)
        }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet =
                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }
    }

    @SuppressLint("DefaultLocale")
    override fun initViews() {
        binding.fabAddAlarm.setOnClickListener {
            val currentTime = LocalTime.now()
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Chọn thời gian nhắc nhở")
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setHour(currentTime.hour)
                .setMinute(currentTime.minute)
                .build()
                .apply {
                    addOnPositiveButtonClickListener {
                        val hour = this.hour
                        val minute = this.minute
                        val notifyTime = String.format("%02d:%02d", hour, minute)
                        viewModel.createAlarm(notifyTime)
                    }
                }
                .show(childFragmentManager, "timePicker")
        }

        adapter = UserAlarmAdapter { userAlarm ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xóa nhắc nhở")
                .setMessage("Bạn có chắc chắn muốn xóa nhắc nhở này?")
                .setNegativeButton("Hủy") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Xóa") { _, _ ->
                    viewModel.deleteAlarm(userAlarm.id)
                    cancelAlarm((userAlarm.id % Int.MAX_VALUE).toInt())
                }
                .show()
        }
        binding.rvAlarm.adapter = adapter
    }

    override fun processArguments(args: Bundle) {
        super.processArguments(args)
        viewModel.prescriptionDetailId = args.getLong("prescriptionDetailId")
    }

    override fun initObservers() {
        viewModel.uiState.onEach { uiState ->
            if (uiState.isLoading) {
                return@onEach
            }

            if (uiState.error != null) {
                (requireActivity() as MainActivity).showToast(uiState.error)
                return@onEach
            }

            if (uiState.success != null) {
                (requireActivity() as MainActivity).showToast(uiState.success)
                return@onEach
            }

            adapter.submitList(uiState.userAlarms)

            uiState.userAlarms.forEach { userAlarm ->
                val hour = userAlarm.notifyTime.split(":")[0].toInt()
                val minute = userAlarm.notifyTime.split(":")[1].toInt()
                scheduleAlarm(
                    (userAlarm.id % Int.MAX_VALUE).toInt(),
                    hour,
                    minute
                )
            }
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun scheduleAlarm(alarmId: Int, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }
        ReminderScheduler.scheduleAlarm(
            requireActivity(),
            calendar,
            alarmId
        )
    }

    private fun cancelAlarm(alarmId: Int) {
        ReminderScheduler.cancelAlarm(
            requireActivity(),
            alarmId
        )
    }
}