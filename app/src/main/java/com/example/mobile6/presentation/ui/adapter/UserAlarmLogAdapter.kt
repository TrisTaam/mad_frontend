package com.example.mobile6.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.data.remote.dto.response.UserAlarmLogResponse
import com.example.mobile6.databinding.ItemUserAlarmLogBinding
import com.example.mobile6.presentation.ui.base.BaseAdapter
import com.example.mobile6.presentation.ui.util.DateUtils.toDateTimeString

class UserAlarmLogAdapter :
    BaseAdapter<UserAlarmLogResponse, UserAlarmLogAdapter.UserAlarmLogViewHolder>(
        simpleDiffCallback(
            areItemsTheSame = { old, new -> old.id == new.id },
            areContentsTheSame = { old, new -> old == new }
        )
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserAlarmLogViewHolder {
        return UserAlarmLogViewHolder(
            ItemUserAlarmLogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: UserAlarmLogViewHolder,
        position: Int
    ) {
        val userAlarmLog = getItem(position)
        holder.bind(userAlarmLog)
    }

    inner class UserAlarmLogViewHolder(private val binding: ItemUserAlarmLogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userAlarmLog: UserAlarmLogResponse) {
            binding.apply {
                tvMedicineName.text = "Uống thuốc ${userAlarmLog.userAlarmResponse.medicineName}"
                tvPrescriptionName.text =
                    "Đơn thuốc ${userAlarmLog.userAlarmResponse.prescriptionName}"
                tvTime.text = userAlarmLog.userAlarmResponse.notifyTime.toDateTimeString()
            }
        }
    }
}