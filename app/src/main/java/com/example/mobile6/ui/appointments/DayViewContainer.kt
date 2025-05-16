package com.example.mobile6.ui.appointments

import android.view.View
import com.example.mobile6.databinding.CalendarDayLayoutBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val binding = CalendarDayLayoutBinding.bind(view)
    lateinit var day: CalendarDay

    var onDayClick: ((CalendarDay) -> Unit)? = null

    init {
        binding.root.setOnClickListener {
            if (::day.isInitialized) {
                onDayClick?.invoke(day)
            }
        }
    }
}