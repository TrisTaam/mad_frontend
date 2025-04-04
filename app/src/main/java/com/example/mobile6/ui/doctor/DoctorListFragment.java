package com.example.mobile6.ui.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.mobile6.R;
import com.example.mobile6.databinding.FragmentDoctorListBinding;

public class DoctorListFragment extends Fragment {
    
    private FragmentDoctorListBinding binding;
    private String specialtyName;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Lấy tên chuyên khoa từ arguments
        if (getArguments() != null) {
            specialtyName = getArguments().getString("specialtyName", "");
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDoctorListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupUI();
        loadDoctors();
    }
    
    private void setupUI() {
        // Thiết lập tiêu đề danh sách
        if (!specialtyName.isEmpty()) {
            binding.listTitle.setText("Danh sách bác sĩ " + specialtyName);
        }
        
        // Thiết lập nút quay lại
        binding.backButton.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }
    
    private void loadDoctors() {
        // Trong thực tế, bạn sẽ tải danh sách bác sĩ từ API hoặc database
        // Ở đây tôi chỉ nạp các ảnh mẫu từ URL đã cho
        String imageUrl = "https://res.cloudinary.com/duzbs23vr/image/upload/v1709048087/uixe1ttzxmksfvmki1sc.png";
        
        // Load ảnh cho các bác sĩ mẫu
        loadDoctorImage(binding.doctorAvatar1, imageUrl);
        loadDoctorImage(binding.doctorAvatar2, imageUrl);
        loadDoctorImage(binding.doctorAvatar3, imageUrl);
        loadDoctorImage(binding.doctorAvatar4, imageUrl);
        
        // Thiết lập sự kiện click cho các item bác sĩ
        binding.doctorItem1.setOnClickListener(v -> {
            // Xử lý khi chọn bác sĩ (có thể chuyển đến trang chi tiết)
        });
        
        binding.doctorItem2.setOnClickListener(v -> {
            // Xử lý khi chọn bác sĩ
        });
        
        binding.doctorItem3.setOnClickListener(v -> {
            // Xử lý khi chọn bác sĩ
        });
        
        binding.doctorItem4.setOnClickListener(v -> {
            // Xử lý khi chọn bác sĩ
        });
    }
    
    private void loadDoctorImage(ImageView imageView, String url) {
        try {
            Glide.with(requireContext())
                    .load(url)
                    .circleCrop()
                    .placeholder(R.drawable.ic_doctor)
                    .error(R.drawable.ic_doctor)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Tránh memory leak
    }
} 