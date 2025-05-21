package com.example.mobile6.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.data.remote.dto.response.ChatUserInfoResponse
import com.example.mobile6.databinding.ItemChatDoctorInDoctorListBinding
import com.example.mobile6.domain.model.Doctor

class MessageAdapter(
    private val onDoctorClick: (Doctor) -> Unit
) : RecyclerView.Adapter<MessageAdapter.DoctorViewHolder>() {

    private val doctors = mutableListOf<Doctor>()
    private val chatUsers = mutableListOf<ChatUserInfoResponse>()

    fun submitList(newList: List<Doctor>) {
        doctors.clear()
        doctors.addAll(newList)
        notifyDataSetChanged()
    }

    fun submitList2(newList: List<ChatUserInfoResponse>) {
        chatUsers.clear()
        chatUsers.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = ItemChatDoctorInDoctorListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        holder.bind(doctors[position])
    }

    override fun getItemCount() = doctors.size

    inner class DoctorViewHolder(private val binding: ItemChatDoctorInDoctorListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(doctor: Doctor) {
            binding.tvDoctorName.text = "${doctor.firstName} ${doctor.lastName}"
            binding.tvDoctorSpecialty.text = doctor.specialty
            // Nếu có avatarUrl thì load bằng Glide/Picasso vào binding.doctorAvatar
            binding.btnSelect.setOnClickListener { onDoctorClick(doctor) }
        }
    }
}