package com.example.mobile6.ui.medicine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobile6.MainApplication;
import com.example.mobile6.R;
import com.example.mobile6.adapter.MedicineAdapter;
import com.example.mobile6.dao.MedicineDao;
import com.example.mobile6.databinding.FragmentMedicineSearchBinding;
import com.example.mobile6.model.Medicine;
import com.example.mobile6.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MedicineSearchFragment extends BaseFragment<FragmentMedicineSearchBinding> {
    private MedicineAdapter medicineAdapter;
    private MedicineDao medicineDao;
    private ExecutorService diskIO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo database
        medicineDao = ((MainApplication) requireActivity().getApplication()).getDatabase().medicineDao();
        diskIO = ((MainApplication) requireActivity().getApplication()).getAppExecutors().diskIO();
    }

    @Override
    protected FragmentMedicineSearchBinding inflateBinding(LayoutInflater layoutInflater, ViewGroup container) {
        return FragmentMedicineSearchBinding.inflate(layoutInflater, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();
        setupUI();
        loadDummyData();
    }

    private void setupUI() {
        // Setup RecyclerView
        medicineAdapter = new MedicineAdapter(medicine -> {
            Bundle bundle = new Bundle();
            bundle.putString("medicineId", medicine.getId());
            navigateTo(R.id.action_medicineSearchFragment_to_medicineDetailFragment, bundle);
        });
        binding.medicinesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.medicinesRecyclerView.setAdapter(medicineAdapter);

        // Setup search functionality
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Tìm kiếm thuốc theo tên
                filterMedicines(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        // Setup back button click
        binding.backButton.setOnClickListener(v -> back());
    }

    private void loadDummyData() {
        try {
            // Tạo dữ liệu mẫu và lưu vào cơ sở dữ liệu
            List<Medicine> medicines = new ArrayList<>();
            medicines.add(Medicine.builder().id("1").name("Panadol Extra 500mg").ingredients("Paracetamol, Caffeine").build());
            medicines.add(Medicine.builder().id("2").name("Efferalgan 500mg").ingredients("Paracetamol").build());
            medicines.add(Medicine.builder().id("3").name("Amoxicillin 500mg").ingredients("Amoxicillin").build());

            // Hiển thị trước (không phụ thuộc database)
            medicineAdapter.submitList(medicines);

            // Lưu vào database
            diskIO.execute(() -> {
                try {
                    medicineDao.insertMedicines(medicines);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterMedicines(String query) {
        try {
            if (query.isEmpty()) {
                // Nếu không có từ khóa, lấy tất cả
                diskIO.execute(() -> {
                    try {
                        List<Medicine> medicines = medicineDao.getAllMedicines();
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> medicineAdapter.submitList(medicines));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                // Tìm kiếm theo từ khóa
                diskIO.execute(() -> {
                    try {
                        List<Medicine> filteredMedicines = medicineDao.searchMedicines(query);
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> medicineAdapter.submitList(filteredMedicines));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 