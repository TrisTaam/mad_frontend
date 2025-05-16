package com.example.mobile6.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.R
import com.example.mobile6.data.remote.dto.response.AppointmentResponse
import com.example.mobile6.databinding.ItemAppointmentBinding
import com.example.mobile6.ui.base.BaseAdapter
import com.example.mobile6.ui.util.DateUtils.toDateTimeString

class AppointmentAdapter :
    BaseAdapter<AppointmentResponse, AppointmentAdapter.AppointmentViewHolder>(
        simpleDiffCallback(
            areItemsTheSame = { old, new -> old.id == new.id },
            areContentsTheSame = { old, new -> old == new }
        )
    ) {
    var onAppointmentClick: (AppointmentResponse) -> Unit = {}
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
        @SuppressLint("SetTextI18n")
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
                        }

                        "APPROVED" -> {
                            text = "Đã duyệt"
                            setTextColor(context.getColor(R.color.green))
                        }

                        "CANCELED" -> {
                            text = "Đã hủy"
                            setTextColor(context.getColor(R.color.red))
                        }

                        else -> "N/A"
                    }
                }
                root.setOnClickListener {
                    onAppointmentClick(appointment)
                }
            }
        }
    }
}