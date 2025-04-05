package com.example.mobile6.ui.medicine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.mobile6.MainApplication;
import com.example.mobile6.R;
import com.example.mobile6.databinding.FragmentMedicineDetailBinding;
import com.example.mobile6.model.MedicineDetail;
import com.example.mobile6.ui.base.BaseFragment;
import com.example.mobile6.util.ImageUtils;

import java.util.concurrent.ExecutorService;

public class MedicineDetailFragment extends BaseFragment<FragmentMedicineDetailBinding> {
    private ExecutorService diskIO;
    private String medicineId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        diskIO = ((MainApplication) requireActivity().getApplication()).getAppExecutors().diskIO();
        medicineId = args.getString("medicineId");
    }

    @Override
    protected FragmentMedicineDetailBinding inflateBinding(LayoutInflater layoutInflater, ViewGroup container) {
        return FragmentMedicineDetailBinding.inflate(layoutInflater, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();
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
        binding.backButton.setOnClickListener(v -> back());

        // Setup add to prescription button
        binding.addToPrescriptionButton.setOnClickListener(v -> showMedicationWarningDialog());

        // Register fragment result listener
        setOnBackResultListener(MedicationWarningDialogFragment.REQUEST_KEY, result -> {
            String action = result.getString(MedicationWarningDialogFragment.RESULT_KEY);
            if (MedicationWarningDialogFragment.RESULT_CONTINUE.equals(action)) {
                // User chose to continue - handle adding to prescription
                addToPrescription();
            } else {
                // User chose to review - do nothing and stay on this screen
            }
        });
    }

    private void showMedicationWarningDialog() {
        navigateTo(R.id.action_medicineDetailFragment_to_medicationWarningDialogFragment);
    }

    private void addToPrescription() {
        // TODO: Implement add to prescription functionality
        back();
    }

    private void loadMedicineDetail(String medicineId) {
        diskIO.execute(() -> {
            try {
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
            ImageUtils.load(medicine.getImageUrl(), binding.medicineImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set text
        binding.medicineDescription.setText(medicine.getDescription());
        binding.medicineUsage.setText(medicine.getUsage());
        binding.medicineDirection.setText(medicine.getDirection());
        binding.medicineWarning.setText(medicine.getWarning());
    }
} 