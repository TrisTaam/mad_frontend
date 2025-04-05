package com.example.mobile6.ui.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.mobile6.R;
import com.example.mobile6.databinding.FragmentDoctorDetailBinding;
import com.example.mobile6.ui.base.BaseFragment;

public class DoctorDetailFragment extends BaseFragment<FragmentDoctorDetailBinding> {
    private String doctorName;
    private String doctorSpecialty;
    private String doctorImageUrl;

    @Override
    protected FragmentDoctorDetailBinding inflateBinding(LayoutInflater layoutInflater, ViewGroup container) {
        return FragmentDoctorDetailBinding.inflate(layoutInflater, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (args != null) {
            doctorName = args.getString("doctorName", "");
            doctorSpecialty = args.getString("doctorSpecialty", "");
            doctorImageUrl = args.getString("doctorImageUrl", "");
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        setupUI();
        loadDoctorInfo();
    }

    private void setupUI() {
        // Set the full name in the header
        if (doctorName != null) {
            // Extract the full name without the "Bác sĩ" prefix if it exists
            String displayName = doctorName;
            if (doctorName.startsWith("Bác sĩ ")) {
                displayName = doctorName.substring(7); // Remove "Bác sĩ " prefix
            }
            binding.headerTitle.setText(displayName);
        }

        // Setup back button
        binding.backButton.setOnClickListener(v -> back());

        // Setup appointment button
        binding.appointmentButton.setOnClickListener(v -> {
            // TODO: Implement appointment scheduling
        });

        // Setup message button
        binding.messageButton.setOnClickListener(v -> {
            // TODO: Implement messaging
        });
    }

    private void loadDoctorInfo() {
        // Set doctor name as "Bác sĩ A" (short form)
        String shortName = "A";
        if (doctorName != null && !doctorName.isEmpty()) {
            String[] nameParts = doctorName.split(" ");
            if (nameParts.length > 0) {
                shortName = nameParts[nameParts.length - 1];
            }
        }
        binding.doctorName.setText("Bác sĩ " + shortName);

        // Set doctor specialty
        binding.doctorSpecialty.setText(doctorSpecialty);

        // Load doctor image
        if (doctorImageUrl != null && !doctorImageUrl.isEmpty()) {
            Glide.with(requireContext())
                    .load(doctorImageUrl)
                    .circleCrop()
                    .placeholder(R.drawable.ic_doctor)
                    .error(R.drawable.ic_doctor)
                    .into(binding.doctorAvatar);
        } else {
            // Use default image if no URL is available
            binding.doctorAvatar.setImageResource(R.drawable.ic_doctor);
        }
    }
} 