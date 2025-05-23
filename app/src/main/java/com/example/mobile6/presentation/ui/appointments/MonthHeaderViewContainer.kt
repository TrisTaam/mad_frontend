package com.example.mobile6.presentation.ui.appointments

import android.view.View
import com.example.mobile6.databinding.CalendarMonthHeaderLayoutBinding
import com.kizitonwose.calendar.view.ViewContainer

class MonthHeaderViewContainer(view: View) : ViewContainer(view) {
    val binding = CalendarMonthHeaderLayoutBinding.bind(view)

    val daysOfWeekTextViews = listOf(
        binding.legendText1,
        binding.legendText2,
        binding.legendText3,
        binding.legendText4,
        binding.legendText5,
        binding.legendText6,
        binding.legendText7
    )
}