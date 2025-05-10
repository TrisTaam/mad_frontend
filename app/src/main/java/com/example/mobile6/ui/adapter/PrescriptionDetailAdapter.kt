package com.example.mobile6.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.data.remote.dto.response.PrescriptionDetailResponse
import com.example.mobile6.databinding.ItemPrescriptionDetailBinding
import kotlinx.coroutines.launch

class PrescriptionDetailAdapter(
    private val onMoreClick: (PrescriptionDetailResponse, View) -> Unit
) : ListAdapter<PrescriptionDetailResponse, PrescriptionDetailAdapter.PrescriptionDetailViewHolder>(
    PrescriptionDetailDiffCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PrescriptionDetailViewHolder {
        val binding = ItemPrescriptionDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PrescriptionDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrescriptionDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PrescriptionDetailViewHolder(
        private val binding: ItemPrescriptionDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: PrescriptionDetailResponse) {
            binding.tvMedicineName.text = detail.medicineName
            val quantityUnit: String = when (detail.quantityUnit.toString().uppercase()) {
                "PILL" -> "Viên"
                "SACHET" -> "Vỉ"
                "PACK" -> "Gói"
                "BOX" -> "Hộp"
                else -> ""
            }
            binding.tvQuantity.text = "${detail.quantity} ${quantityUnit}"
            binding.tvDosage.text = detail.quantityUsage

            binding.ivMore.setOnClickListener {
                onMoreClick(detail, it)
            }
        }
    }

    private class PrescriptionDetailDiffCallback :
        DiffUtil.ItemCallback<PrescriptionDetailResponse>() {
        override fun areItemsTheSame(
            oldItem: PrescriptionDetailResponse,
            newItem: PrescriptionDetailResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PrescriptionDetailResponse,
            newItem: PrescriptionDetailResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
}