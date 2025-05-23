package com.example.mobile6.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.data.remote.dto.response.UserAlarmResponse
import com.example.mobile6.databinding.ItemUserAlarmBinding
import com.example.mobile6.presentation.ui.base.BaseAdapter

class UserAlarmAdapter(
    private val onDeleteClick: (UserAlarmResponse) -> Unit
) : BaseAdapter<UserAlarmResponse, UserAlarmAdapter.UserAlarmViewHolder>(
    simpleDiffCallback(
        areItemsTheSame = { old, new -> old.id == new.id },
        areContentsTheSame = { old, new -> old == new }
    )
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserAlarmViewHolder {
        return UserAlarmViewHolder(
            ItemUserAlarmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: UserAlarmViewHolder,
        position: Int
    ) {
        val userAlarm = getItem(position)
        holder.bind(userAlarm)
    }

    inner class UserAlarmViewHolder(private val binding: ItemUserAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userAlarm: UserAlarmResponse) {
            binding.apply {
                tvTime.text = userAlarm.notifyTime
                btnDelete.setOnClickListener {
                    onDeleteClick(userAlarm)
                }
            }
        }
    }
}