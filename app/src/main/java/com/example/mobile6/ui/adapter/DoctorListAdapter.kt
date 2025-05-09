package com.example.mobile6.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobile6.R
import com.example.mobile6.databinding.ItemDoctorBinding
import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.ui.base.BaseAdapter

class DoctorListAdapter(
    private val onDoctorClick: (Doctor) -> Unit
) : BaseAdapter<Doctor, DoctorListAdapter.DoctorListViewHolder>(
    simpleDiffCallback<Doctor>(
        areItemsTheSame = { old, new -> old.id == new.id },
        areContentsTheSame = { old, new -> old == new }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorListViewHolder {
        return DoctorListViewHolder(
            ItemDoctorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DoctorListViewHolder, position: Int) {
        val doctor = getItem(position)
        holder.bind(doctor)
    }

    inner class DoctorListViewHolder(private val binding: ItemDoctorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(doctor: Doctor) {
            binding.apply {
                doctorName.text = "${doctor.lastName} ${doctor.firstName}"
                doctorSpecialty.text = doctor.specialty
                Glide.with(binding.root.context)
                    .load(doctor.avatar)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(doctorAvatar)
                root.setOnClickListener { onDoctorClick(doctor) }
            }
        }
    }
}