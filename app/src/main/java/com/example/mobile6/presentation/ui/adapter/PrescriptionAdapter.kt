package com.example.mobile6.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.R
import com.example.mobile6.data.remote.dto.response.PrescriptionResponse
import com.example.mobile6.databinding.ItemPrescriptionBinding
import com.example.mobile6.presentation.ui.base.BaseAdapter
import com.example.mobile6.presentation.ui.util.DateUtils.toUtilDate
import com.example.mobile6.presentation.ui.util.DateUtils.toddMMyyyyString

class PrescriptionAdapter() :
    BaseAdapter<PrescriptionResponse, PrescriptionAdapter.PrescriptionViewHolder>(
        simpleDiffCallback(
            areItemsTheSame = { old, new -> old.prescriptionId == new.prescriptionId },
            areContentsTheSame = { old, new -> old == new }
        )
    ) {
    var onPrescriptionClick: (PrescriptionResponse) -> Unit = {}
    var isDoctorMode: Boolean = false
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PrescriptionViewHolder {
        return PrescriptionViewHolder(
            ItemPrescriptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: PrescriptionViewHolder,
        position: Int
    ) {
        val prescription = getItem(position)
        holder.bind(prescription)
    }

    inner class PrescriptionViewHolder(private val binding: ItemPrescriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(prescription: PrescriptionResponse) {
            binding.apply {
                tvName.text = prescription.prescriptionName
                tvUserName.text = if (isDoctorMode) {
                    "Bệnh nhân: ${prescription.userName ?: "N/A"}"
                } else {
                    "Bác sĩ: ${prescription.doctorName ?: "N/A"}"
                }
                tvDate.text = prescription.prescriptionDate.toUtilDate().toddMMyyyyString()
                with(tvStatus) {
                    when (prescription.status) {
                        "CREATED" -> {
                            text = "Đã tạo"
                            setTextColor(context.getColor(R.color.yellow))
                        }

                        "ACTIVE" -> {
                            text = "Đang sử dụng"
                            setTextColor(context.getColor(R.color.green))
                        }

                        "DEACTIVATED" -> {
                            text = "Đã hoàn thành"
                            setTextColor(context.getColor(R.color.red))
                        }

                        else -> {
                            text = "N/A"
                        }
                    }
                }
                root.setOnClickListener {
                    onPrescriptionClick(prescription)
                }
            }
        }
    }
}