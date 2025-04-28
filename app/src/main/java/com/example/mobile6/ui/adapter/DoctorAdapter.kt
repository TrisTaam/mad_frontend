package com.example.mobile6.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.databinding.ItemDoctorBinding
import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.ui.base.BaseAdapter

class DoctorAdapter(
    private val onMedicineClick: (Doctor) -> Unit
) : BaseAdapter<Doctor, DoctorAdapter.DoctorViewHolder>(
    simpleDiffCallback<Doctor>(
        areItemsTheSame = { old, new -> old.id == new.id },
        areContentsTheSame = { old, new -> old == new }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        return DoctorViewHolder(
            ItemDoctorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = getItem(position)
        holder.bind(doctor)
    }

    inner class DoctorViewHolder(private val binding: ItemDoctorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(doctor: Doctor) {
            binding.apply {
                doctorSpecialty.text = doctor.specialty
                root.setOnClickListener { onMedicineClick(doctor) }
            }
        }
    }
}