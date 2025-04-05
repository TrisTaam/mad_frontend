package com.example.mobile6.ui.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobile6.R;
import com.example.mobile6.databinding.FragmentDoctorSpecialtyBinding;
import com.example.mobile6.ui.base.BaseFragment;

public class DoctorSpecialtyFragment extends BaseFragment<FragmentDoctorSpecialtyBinding> {

    @Override
    protected FragmentDoctorSpecialtyBinding inflateBinding(LayoutInflater layoutInflater, ViewGroup container) {
        return FragmentDoctorSpecialtyBinding.inflate(layoutInflater, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();
        setupUI();
    }

    private void setupUI() {
        // Khởi tạo các thành phần UI và thiết lập sự kiện
        binding.backButton.setOnClickListener(v -> {
            // Quay lại màn hình trước đó
            back();
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
            navigateTo(R.id.action_doctorSpecialtyFragment_to_doctorListFragment, args);
        });
    }
}
