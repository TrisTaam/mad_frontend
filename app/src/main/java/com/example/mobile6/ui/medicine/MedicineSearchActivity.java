package com.example.mobile6.ui.medicine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile6.MainApplication;
import com.example.mobile6.R;
import com.example.mobile6.adapter.MedicineAdapter;
import com.example.mobile6.dao.MedicineDao;
import com.example.mobile6.model.Medicine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MedicineSearchActivity extends AppCompatActivity implements MedicineAdapter.OnMedicineClickListener {

    private static final int REQUEST_CODE = 100;

    private RecyclerView medicinesRecyclerView;
    private MedicineAdapter medicineAdapter;
    private EditText searchEditText;
    private ImageButton backButton;
    private MedicineDao medicineDao;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_search);

        // Khởi tạo database
        medicineDao = ((MainApplication) getApplication()).getDatabase().medicineDao();
        executorService = Executors.newSingleThreadExecutor();

        // Initialize views
        medicinesRecyclerView = findViewById(R.id.medicines_recycler_view);
        searchEditText = findViewById(R.id.search_edit_text);
        backButton = findViewById(R.id.back_button);

        // Setup RecyclerView
        medicineAdapter = new MedicineAdapter(this);
        medicinesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicinesRecyclerView.setAdapter(medicineAdapter);

        // Load dummy data for demo purposes
        loadDummyData();

        // Setup search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
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
        backButton.setOnClickListener(v -> finish());
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
                        runOnUiThread(() -> medicineAdapter.setMedicines(medicines));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                // Tìm kiếm theo từ khóa
                executorService.execute(() -> {
                    try {
                        List<Medicine> filteredMedicines = medicineDao.searchMedicines(query);
                        runOnUiThread(() -> medicineAdapter.setMedicines(filteredMedicines));
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
        // Handle medicine selection
        // In a real app, you would pass the selected medicine back to the calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("MEDICINE_ID", medicine.getId());
        resultIntent.putExtra("MEDICINE_NAME", medicine.getName());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}