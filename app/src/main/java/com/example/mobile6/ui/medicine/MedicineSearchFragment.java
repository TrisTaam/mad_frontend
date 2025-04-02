package com.example.mobile6.ui.medicine;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobile6.MainApplication;
import com.example.mobile6.adapter.MedicineAdapter;
import com.example.mobile6.dao.MedicineDao;
import com.example.mobile6.databinding.FragmentMedicineSearchBinding;
import com.example.mobile6.model.Medicine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MedicineSearchFragment extends Fragment implements MedicineAdapter.OnMedicineClickListener {

    private FragmentMedicineSearchBinding binding;
    private MedicineAdapter medicineAdapter;
    private MedicineDao medicineDao;
    private ExecutorService executorService;
    private OnMedicineSelectedListener medicineSelectedListener;

    public interface OnMedicineSelectedListener {
        void onMedicineSelected(Medicine medicine);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            medicineSelectedListener = (OnMedicineSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMedicineSelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo database
        medicineDao = ((MainApplication) requireActivity().getApplication()).getDatabase().medicineDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMedicineSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUI();
        loadDummyData();
    }

    private void setupUI() {
        // Setup RecyclerView
        medicineAdapter = new MedicineAdapter(this);
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
        binding.backButton.setOnClickListener(v -> {
            // Sử dụng Navigation Component để quay lại
            Navigation.findNavController(requireView()).popBackStack();
        });
    }

    private void loadDummyData() {
        try {
            // Tạo dữ liệu mẫu và lưu vào cơ sở dữ liệu
            List<Medicine> medicines = new ArrayList<>();
            medicines.add(Medicine.builder().id("1").name("Panadol Extra 500mg").ingredients("Paracetamol, Caffeine").build());
            medicines.add(Medicine.builder().id("2").name("Efferalgan 500mg").ingredients("Paracetamol").build());
            medicines.add(Medicine.builder().id("3").name("Amoxicillin 500mg").ingredients("Amoxicillin").build());

            // Hiển thị trước (không phụ thuộc database)
            medicineAdapter.setMedicines(medicines);

            // Lưu vào database
            executorService.execute(() -> {
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
                executorService.execute(() -> {
                    try {
                        List<Medicine> medicines = medicineDao.getAllMedicines();
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> medicineAdapter.setMedicines(medicines));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                // Tìm kiếm theo từ khóa
                executorService.execute(() -> {
                    try {
                        List<Medicine> filteredMedicines = medicineDao.searchMedicines(query);
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> medicineAdapter.setMedicines(filteredMedicines));
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

    @Override
    public void onMedicineClick(Medicine medicine) {
        if (medicineSelectedListener != null) {
            medicineSelectedListener.onMedicineSelected(medicine);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
} 