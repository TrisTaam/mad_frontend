package com.example.mobile6.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.R
import com.example.mobile6.data.remote.dto.response.AppointmentResponse
import com.example.mobile6.databinding.ItemAppointmentBinding
import com.example.mobile6.presentation.ui.base.BaseAdapter
import com.example.mobile6.presentation.ui.util.DateUtils.toDateTimeString

class AppointmentAdapter :
    BaseAdapter<AppointmentResponse, AppointmentAdapter.AppointmentViewHolder>(
        simpleDiffCallback(
            areItemsTheSame = { old, new -> old.id == new.id },
            areContentsTheSame = { old, new -> old == new }
        )
    ) {
    var onAppointmentClick: (AppointmentResponse, View, Int, Int) -> Unit = { _, _, _, _ -> }
    var isDoctorMode: Boolean = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppointmentViewHolder {
        return AppointmentViewHolder(
            ItemAppointmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: AppointmentViewHolder,
        position: Int
    ) {
        val appointment = getItem(position)
        holder.bind(appointment)
    }

    inner class AppointmentViewHolder(private val binding: ItemAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
        fun bind(appointment: AppointmentResponse) {
            binding.apply {
                tvUserName.text = if (isDoctorMode) {
                    "Bệnh nhân: ${appointment.userName}"
                } else {
                    "Bác sĩ: ${appointment.doctorName}"
                }
                tvDate.text = appointment.appointmentDate.toDateTimeString()
                with(tvStatus) {
                    when (appointment.status) {
                        "PENDING" -> {
                            text = "Đang chờ duyệt"
                            setTextColor(context.getColor(R.color.yellow))
                            setBackgroundResource(R.drawable.bg_yellow_status)
                        }

                        "APPROVED" -> {
                            text = "Đã duyệt"
                            setTextColor(context.getColor(R.color.green))
                            setBackgroundResource(R.drawable.bg_green_status)
                        }

                        "CANCELED" -> {
                            text = "Đã hủy"
                            setTextColor(context.getColor(R.color.red))
                            setBackgroundResource(R.drawable.bg_red_status)
                        }

                        else -> {
                            text = "N/A"
                        }
                    }
                }
                root.setOnTouchListener { view, event ->
                    val x = event.rawX.toInt()
                    val y = event.rawY.toInt()
                    onAppointmentClick(appointment, view, x, y)
                    false
                }
            }
        }
    }
}