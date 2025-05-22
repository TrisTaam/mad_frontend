package com.example.mobile6.presentation.ui.appointments

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentAppointmentsBinding
import com.example.mobile6.databinding.ItemAppointmentStatusMenuBinding
import com.example.mobile6.presentation.ui.MainActivity
import com.example.mobile6.presentation.ui.adapter.AppointmentAdapter
import com.example.mobile6.presentation.ui.base.BaseFragment
import com.example.mobile6.presentation.ui.util.gone
import com.example.mobile6.presentation.ui.util.visible
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

@AndroidEntryPoint
class AppointmentsFragment : BaseFragment<FragmentAppointmentsBinding>() {
    private val viewModel: AppointmentsViewModel by viewModels()

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    private lateinit var adapter: AppointmentAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentAppointmentsBinding
        get() = { layoutInflater, container ->
            FragmentAppointmentsBinding.inflate(layoutInflater, container, false)
        }

    override fun initViews() {
        binding.backButton.setOnClickListener {
            back()
        }
        binding.fabAddAppointment.setOnClickListener {
            navigateTo(R.id.action_appointmentsFragment_to_doctorSpecialtyFragment)
        }
        adapter = AppointmentAdapter()
        binding.rvAppointments.adapter = adapter
        setupCalendar()
    }

    override fun initObservers() {
        viewModel.uiState.onEach { uiState ->
            if (uiState.isLoading) {
                return@onEach
            }
            if (uiState.success != null) {
                (requireActivity() as MainActivity).showToast(uiState.success)
                return@onEach
            }
            if (uiState.error != null) {
                (requireActivity() as MainActivity).showToast(uiState.error)
                return@onEach
            }
            handleMode(uiState.isDoctorMode)

            adapter.isDoctorMode = uiState.isDoctorMode
            adapter.submitList(uiState.appointments)
            onDaySelected(selectedDate ?: today)
            binding.calendarView.notifyCalendarChanged()
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleMode(isDoctorMode: Boolean) {
        with(binding.fabAddAppointment) {
            if (isDoctorMode) {
                gone()
            } else {
                visible()
            }
        }
        if (isDoctorMode) {
            adapter.onAppointmentClick = { appointment, view, x, y ->
                if (appointment.status == "PENDING") {
                    val popupWindow = PopupWindow(requireContext())
                    val menuBinding =
                        ItemAppointmentStatusMenuBinding.inflate(
                            LayoutInflater.from(
                                requireContext()
                            )
                        )

                    popupWindow.contentView = menuBinding.root
                    popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
                    popupWindow.elevation = resources.getDimension(R.dimen.elevation_4dp)
                    popupWindow.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                    popupWindow.isFocusable = true

                    menuBinding.tvApprove.setOnClickListener {
                        viewModel.approveAppointment(appointment.id)
                        popupWindow.dismiss()
                    }

                    menuBinding.tvCancel.setOnClickListener {
                        viewModel.cancelAppointment(appointment.id)
                        popupWindow.dismiss()
                    }

                    // Show popup
                    popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y)
                }
            }
        }
    }

    private fun setupCalendar() {
        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)

        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view).apply {
                onDayClick = { day ->
                    when (day.position) {
                        DayPosition.MonthDate -> {
                            onDaySelected(day.date)
                        }

                        DayPosition.InDate, DayPosition.OutDate -> {
                            scrollToMonth(day.date.yearMonth)
                            onDaySelected(day.date)
                        }
                    }
                }
            }

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                bindDay(container, data)
            }
        }

        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthHeaderViewContainer> {
                override fun create(view: View) = MonthHeaderViewContainer(view)
                override fun bind(container: MonthHeaderViewContainer, data: CalendarMonth) {
                    for (i in daysOfWeek.indices) {
                        container.daysOfWeekTextViews[i].text = when (i) {
                            0 -> "H"
                            1 -> "B"
                            2 -> "T"
                            3 -> "N"
                            4 -> "S"
                            5 -> "B"
                            else -> "CN"
                        }
                    }
                }
            }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(12)
        val endMonth = currentMonth.plusMonths(12)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        scrollToMonth(currentMonth)

        binding.calendarView.monthScrollListener = { month ->
            updateMonthTitle(month)
        }
    }

    private fun scrollToMonth(month: YearMonth) {
        binding.calendarView.scrollToMonth(month)
    }

    private fun bindDay(container: DayViewContainer, day: CalendarDay) {
        val textView = container.binding.dayText
        val dotView = container.binding.eventDot

        textView.text = day.date.dayOfMonth.toString()

        if (day.position != DayPosition.MonthDate) {
            textView.alpha = 0.3f
            dotView.gone()
            return
        } else {
            textView.alpha = 1f
            dotView.visible()
        }

        if (selectedDate == day.date) {
            textView.setBackgroundResource(R.drawable.selected_background)
        } else if (day.date == today) {
            textView.setBackgroundResource(R.drawable.today_background)
        } else {
            textView.background = null
        }

        val hasEvent = viewModel.appointmentsGroupDate.keys.any { it == day.date }
        with(dotView) {
            if (hasEvent) {
                visible()
            } else {
                gone()
            }
        }
    }

    private fun updateMonthTitle(month: CalendarMonth) {
        binding.monthYearText.text = monthTitleFormatter.format(month.yearMonth)
    }

    private fun onDaySelected(date: LocalDate) {
        val oldDate = selectedDate
        selectedDate = date

        oldDate?.let { binding.calendarView.notifyDateChanged(it) }
        binding.calendarView.notifyDateChanged(date)

        viewModel.selectDate(date)
    }
}