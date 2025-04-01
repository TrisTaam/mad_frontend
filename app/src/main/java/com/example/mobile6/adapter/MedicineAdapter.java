package com.example.mobile6.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile6.R;
import com.example.mobile6.model.Medicine;

import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private List<Medicine> medicines = new ArrayList<>();
    private OnMedicineClickListener listener;

    public interface OnMedicineClickListener {
        void onMedicineClick(Medicine medicine);
    }

    public MedicineAdapter(OnMedicineClickListener listener) {
        this.listener = listener;
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
        notifyDataSetChanged();
    }

    public void filterMedicines(String query) {
        // Method for filtering the medicines list based on search query
        // To be implemented based on your data source
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        holder.medicineName.setText(medicine.getName());
        holder.medicineIngredients.setText("Thành phần: " + medicine.getIngredients());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMedicineClick(medicine);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView medicineName;
        TextView medicineIngredients;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicine_name);
            medicineIngredients = itemView.findViewById(R.id.medicine_ingredients);
        }
    }
} 