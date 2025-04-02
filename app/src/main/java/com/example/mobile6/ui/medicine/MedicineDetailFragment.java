package com.example.mobile6.ui.medicine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.mobile6.MainApplication;
import com.example.mobile6.R;
import com.example.mobile6.dao.MedicineDao;
import com.example.mobile6.databinding.FragmentMedicineDetailBinding;
import com.example.mobile6.model.Medicine;
import com.example.mobile6.model.MedicineDetail;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MedicineDetailFragment extends Fragment {

    private FragmentMedicineDetailBinding binding;
    private MedicineDao medicineDao;
    private ExecutorService executorService;
    private String medicineId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        medicineDao = ((MainApplication) requireActivity().getApplication()).getDatabase().medicineDao();
        executorService = Executors.newSingleThreadExecutor();
        
        // Get medicineId from arguments
        if (getArguments() != null) {
            medicineId = getArguments().getString("medicineId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMedicineDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupUI();
        if (medicineId != null) {
            loadMedicineDetail(medicineId);
        } else {
            // For testing, load dummy data
            loadDummyData();
        }
    }

    private void setupUI() {
        // Setup back button
        binding.backButton.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).popBackStack();
        });
        
        // Setup add to prescription button
        binding.addToPrescriptionButton.setOnClickListener(v -> {
            // TODO: Implement add to prescription functionality
            Navigation.findNavController(requireView()).popBackStack();
        });
    }

    private void loadMedicineDetail(String medicineId) {
        executorService.execute(() -> {
            try {
                // In a real app, you would fetch this from the database
                // For now, we'll use dummy data
                MedicineDetail medicine = createDummyMedicineDetail(medicineId);
                
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> displayMedicineDetail(medicine));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadDummyData() {
        MedicineDetail medicine = createDummyMedicineDetail("1");
        displayMedicineDetail(medicine);
    }

    private MedicineDetail createDummyMedicineDetail(String id) {
        if ("1".equals(id)) {
            return MedicineDetail.detailBuilder()
                    .id("1")
                    .name("Panadol Extra 500mg")
                    .ingredients("Paracetamol, Caffeine")
                    .description("Panadol Extra là một loại thuốc giảm đau có chứa Paracetamol và Caffeine, giúp giảm đau nhanh hơn so với Panadol thông thường")
                    .usage("Giảm đau từ nhẹ đến trung bình, hạ sốt đo cảm cúm sốt virut")
                    .direction("Người lớn & trẻ trên 12 tuổi: Uống 1-2 viên/lần, cách nhau ít nhất 4 giờ.")
                    .warning("Tránh dùng chung với các thuốc khác có chứa Paracetamol để tránh quá liều")
                    .imageUrl("https://res.cloudinary.com/duzbs23vr/image/upload/v1709048087/uixe1ttzxmksfvmki1sc.png")
                    .build();
        } else if ("2".equals(id)) {
            return MedicineDetail.detailBuilder()
                    .id("2")
                    .name("Efferalgan 500mg")
                    .ingredients("Paracetamol")
                    .description("Efferalgan chứa hoạt chất Paracetamol, có tác dụng giảm đau và hạ sốt hiệu quả")
                    .usage("Giảm đau và hạ sốt trong các trường hợp cảm cúm, đau đầu, đau răng")
                    .direction("Người lớn và trẻ em > 10 tuổi: 1-2 viên/lần, 3-4 lần/ngày")
                    .warning("Không dùng quá liều quy định, không dùng kéo dài")
                    .imageUrl("https://res.cloudinary.com/duzbs23vr/image/upload/v1709048087/uixe1ttzxmksfvmki1sc.png")
                    .build();
        } else {
            return MedicineDetail.detailBuilder()
                    .id("3")
                    .name("Amoxicillin 500mg")
                    .ingredients("Amoxicillin")
                    .description("Amoxicillin là kháng sinh nhóm beta-lactam, có tác dụng diệt khuẩn phổ rộng")
                    .usage("Điều trị các bệnh nhiễm khuẩn đường hô hấp, tiêu hóa, tiết niệu")
                    .direction("Người lớn: 500mg, 3 lần/ngày. Trẻ em: theo chỉ định của bác sĩ")
                    .warning("Dùng đủ liệu trình, không tự ý dừng thuốc khi thấy đỡ. Có thể gây dị ứng")
                    .imageUrl("https://res.cloudinary.com/duzbs23vr/image/upload/v1709048087/uixe1ttzxmksfvmki1sc.png")
                    .build();
        }
    }

    private void displayMedicineDetail(MedicineDetail medicine) {
        binding.headerTitle.setText("Thông tin thuốc");
        
        // Load image
        try {
            Glide.with(requireContext())
                    .load(medicine.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.medicineImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Set text
        binding.medicineDescription.setText(medicine.getDescription());
        binding.medicineUsage.setText(medicine.getUsage());
        binding.medicineDirection.setText(medicine.getDirection());
        binding.medicineWarning.setText(medicine.getWarning());
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