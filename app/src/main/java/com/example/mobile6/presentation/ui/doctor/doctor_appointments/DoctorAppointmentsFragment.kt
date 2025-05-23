package com.example.mobile6.presentation.ui.doctor.doctor_appointments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mobile6.databinding.FragmentDoctorAppointmentsBinding
import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class DoctorAppointmentsFragment : BaseFragment<FragmentDoctorAppointmentsBinding>() {

    private val viewModel: DoctorAppointmentsViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentDoctorAppointmentsBinding
        get() = { layoutInflater, container ->
            FragmentDoctorAppointmentsBinding.inflate(layoutInflater, container, false)
        }

    private var doctor: Doctor? = null
    private val calendar = Calendar.getInstance()

    override fun processArguments(args: Bundle) {
        doctor = arguments?.getParcelable("doctor")
    }

    override fun initViews() {
        setupHeader()
        setupDoctorInfo()
        setupDatePicker()
        setupTimePicker()
        setupConfirmButton()
    }

    private fun setupHeader() {
        binding.backButton.setOnClickListener {
            back()
        }
    }

    private fun setupDatePicker() {
        binding.pickDateButton.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                calendar.set(Calendar.YEAR, selectedYear)
                calendar.set(Calendar.MONTH, selectedMonth)
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay)
                updateSelectedDateText()
            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

    private fun updateSelectedDateText() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.selectedDateText.text = dateFormat.format(calendar.time)
    }

    private fun setupTimePicker() {
        binding.pickTimeButton.setOnClickListener {
            showTimePickerDialog()
        }
    }

    private fun showTimePickerDialog() {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)
                updateSelectedTimeText()
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()
    }

    private fun updateSelectedTimeText() {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        binding.selectedTimeText.text = timeFormat.format(calendar.time)
    }

    private fun setupDoctorInfo() {
        doctor?.let { doctor ->
            binding.doctorName.text = "${doctor.lastName} ${doctor.firstName} - ${doctor.specialty}"
        }
    }

    private fun setupConfirmButton() {
        binding.confirmAppointmentButton.setOnClickListener {
            if (validateInputs()) {
                createAppointment()
            }
        }
    }

    private fun validateInputs(): Boolean {
        if (binding.selectedDateText.text.toString() == "Chưa chọn ngày") {
            Toast.makeText(requireContext(), "Vui lòng chọn ngày hẹn", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.selectedTimeText.text.toString() == "Chưa chọn giờ") {
            Toast.makeText(requireContext(), "Vui lòng chọn giờ hẹn", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun createAppointment() {
        doctor?.let { viewModel.createAppointment(it.id, calendar) }
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
                    if (state.isSuccess) {
                        Toast.makeText(
                            requireContext(),
                            "Đặt lịch hẹn thành công vào ${binding.selectedDateText.text}, ${binding.selectedTimeText.text}",
                            Toast.LENGTH_LONG
                        ).show()
                        back()
                        back()
                        back()
                        back()
                        return@collectLatest
                    }
                }
            }
        }
    }

    private fun handleLoadingState(isLoading: Boolean) {
    }

    private fun handleErrorState(error: String?) {
        error?.let {
            Toast.makeText(requireContext(), "Lỗi: ${it}", Toast.LENGTH_LONG).show()
        }
    }
}