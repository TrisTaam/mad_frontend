package com.example.mobile6.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.databinding.ItemMedicineBinding
import com.example.mobile6.domain.model.Medicine
import com.example.mobile6.ui.base.BaseAdapter

class MedicineAdapter(
    private val onMedicineClick: (Medicine) -> Unit
) : BaseAdapter<Medicine, MedicineAdapter.MedicineViewHolder>(
    simpleDiffCallback<Medicine>(
        areItemsTheSame = { old, new -> old.id == new.id },
        areContentsTheSame = { old, new -> old == new }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        return MedicineViewHolder(
            ItemMedicineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val medicine = getItem(position)
        holder.bind(medicine)
    }

    inner class MedicineViewHolder(private val binding: ItemMedicineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(medicine: Medicine) {
            binding.apply {
                medicineName.text = medicine.name
                
                val ingredientsText = if (medicine.ingredients.isNotEmpty()) {
                    "Thành phần: " + medicine.ingredients.joinToString(", ") { it.name }
                } else {
                    "Chưa có thông tin thành phần"
                }
                medicineIngredients.text = ingredientsText
                
                root.setOnClickListener { onMedicineClick(medicine) }
            }
        }
    }
}
