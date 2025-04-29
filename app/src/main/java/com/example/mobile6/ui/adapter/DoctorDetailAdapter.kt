package com.example.mobile6.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.databinding.ItemDoctorDescriptionBinding
import com.example.mobile6.domain.model.DoctorInfo
import com.example.mobile6.ui.base.BaseAdapter

class DoctorDetailAdapter(
    private val onDoctorDetailClick: (DoctorInfo) -> Unit
) : BaseAdapter<DoctorInfo, DoctorDetailAdapter.DoctorDetailViewHolder>(
    simpleDiffCallback<DoctorInfo>(
        areItemsTheSame = { old, new -> old.id == new.id },
        areContentsTheSame = { old, new -> old == new }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorDetailViewHolder {
        return DoctorDetailViewHolder(
            ItemDoctorDescriptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DoctorDetailViewHolder, position: Int) {
        val doctorInfo = getItem(position)
        holder.bind(doctorInfo)
    }

    inner class DoctorDetailViewHolder(private val binding: ItemDoctorDescriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(doctorInfo: DoctorInfo) {
            binding.apply {
                doctorDescription.text = doctorInfo.title
                doctorDescriptionDetail.text = doctorInfo.description
                root.setOnClickListener { onDoctorDetailClick(doctorInfo) }
            }
        }
    }
}