package com.example.mobile6.ui.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mobile6.R;
import com.example.mobile6.databinding.FragmentDoctorSpecialtyBinding;

public class DoctorSpecialtyFragment extends Fragment {
    
    private FragmentDoctorSpecialtyBinding binding;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDoctorSpecialtyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUI();
    }
    
    private void setupUI() {
        // Khởi tạo các thành phần UI và thiết lập sự kiện
        binding.backButton.setOnClickListener(v -> {
            // Quay lại màn hình trước đó
            requireActivity().onBackPressed();
        });
        
        // Thiết lập sự kiện click cho các mục chuyên khoa
        setupSpecialtyItemClick(binding.specialtyItem1, "khoa sản");
        setupSpecialtyItemClick(binding.specialtyItem2, "khoa sản");
        setupSpecialtyItemClick(binding.specialtyItem3, "khoa sản");
    }
    
    private void setupSpecialtyItemClick(View item, String specialtyName) {
        item.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("specialtyName", specialtyName);
            
            // Chuyển đến màn hình danh sách bác sĩ với tên chuyên khoa
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_doctorSpecialtyFragment_to_doctorListFragment, args);
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}
