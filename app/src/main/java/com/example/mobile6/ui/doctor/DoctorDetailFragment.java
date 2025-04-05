package com.example.mobile6.ui.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mobile6.R;
import com.example.mobile6.databinding.FragmentDoctorDetailBinding;

public class DoctorDetailFragment extends Fragment {
    
    private FragmentDoctorDetailBinding binding;
    private String doctorName;
    private String doctorSpecialty;
    private String doctorImageUrl;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get doctor information from arguments
        if (getArguments() != null) {
            doctorName = getArguments().getString("doctorName", "");
            doctorSpecialty = getArguments().getString("doctorSpecialty", "");
            doctorImageUrl = getArguments().getString("doctorImageUrl", "");
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDoctorDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupUI();
        loadDoctorInfo();
    }
    
    private void setupUI() {
        // Get the doctor's short name (just "A" from "Bác sĩ Lê Văn A")
        String shortName = "A";
        if (doctorName != null && !doctorName.isEmpty()) {
            String[] nameParts = doctorName.split(" ");
            if (nameParts.length > 0) {
                shortName = nameParts[nameParts.length - 1];
            }
        }
        
        // Set the full name in the header
        TextView headerTitle = binding.headerLayout.findViewById(R.id.header_title);
        if (headerTitle != null && doctorName != null) {
            // Extract the full name without the "Bác sĩ" prefix if it exists
            String displayName = doctorName;
            if (doctorName.startsWith("Bác sĩ ")) {
                displayName = doctorName.substring(7); // Remove "Bác sĩ " prefix
            }
            headerTitle.setText(displayName);
        }
        
        // Setup back button
        binding.backButton.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        
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
            // Use Glide to load image if URL is available with circleCrop
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