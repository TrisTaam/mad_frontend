package com.example.mobile6.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile6.databinding.ItemMedicineBinding;
import com.example.mobile6.model.Medicine;
import com.example.mobile6.ui.base.BaseAdapter;

import java.util.function.Consumer;

public class MedicineAdapter extends BaseAdapter<Medicine, MedicineAdapter.MedicineViewHolder> {
    private final Consumer<Medicine> onMedicineClick;

    public MedicineAdapter(Consumer<Medicine> onMedicineClick) {
        super(new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull Medicine oldItem, @NonNull Medicine newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Medicine oldItem, @NonNull Medicine newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.onMedicineClick = onMedicineClick;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MedicineViewHolder(
                ItemMedicineBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                ),
                onMedicineClick
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = getItem(position);
        holder.bind(medicine);
    }

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
        private ItemMedicineBinding binding;
        private final Consumer<Medicine> onMedicineClick;

        public MedicineViewHolder(@NonNull ItemMedicineBinding binding, Consumer<Medicine> onMedicineClick) {
            super(binding.getRoot());
            this.binding = binding;
            this.onMedicineClick = onMedicineClick;
        }

        public void bind(Medicine medicine) {
            binding.medicineName.setText(medicine.getName());
            binding.medicineIngredients.setText("Thành phần: " + medicine.getIngredients());
            binding.getRoot().setOnClickListener(v -> {
                if (onMedicineClick != null) {
                    onMedicineClick.accept(medicine);
                }
            });
        }
    }
}