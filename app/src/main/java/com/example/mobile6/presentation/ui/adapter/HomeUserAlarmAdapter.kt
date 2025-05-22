package com.example.mobile6.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.data.remote.dto.response.UserAlarmResponse
import com.example.mobile6.databinding.ItemHomeUserAlarmBinding
import com.example.mobile6.presentation.ui.base.BaseAdapter
import java.time.LocalTime

class HomeUserAlarmAdapter(
    private val onClick: (UserAlarmResponse) -> Unit
) : BaseAdapter<UserAlarmResponse, HomeUserAlarmAdapter.HomeUserAlarmViewHolder>(
    simpleDiffCallback(
        areItemsTheSame = { old, new -> old.id == new.id },
        areContentsTheSame = { old, new -> old == new }
    )
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeUserAlarmViewHolder {
        return HomeUserAlarmViewHolder(
            ItemHomeUserAlarmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: HomeUserAlarmViewHolder,
        position: Int
    ) {
        val userAlarm = getItem(position)
        holder.bind(userAlarm)
    }

    inner class HomeUserAlarmViewHolder(private val binding: ItemHomeUserAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(userAlarm: UserAlarmResponse) {
            binding.apply {
                tvMedicineName.text = userAlarm.medicineName
                tvPrescriptionName.text = "Đơn thuốc: ${userAlarm.prescriptionName}"
                tvTime.text = LocalTime.parse(userAlarm.notifyTime).toString()
                root.setOnClickListener {
                    onClick(userAlarm)
                }
            }
        }
    }
}