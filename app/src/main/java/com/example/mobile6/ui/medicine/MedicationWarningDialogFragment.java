package com.example.mobile6.ui.medicine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.mobile6.databinding.DialogMedicationWarningBinding;
import com.example.mobile6.ui.base.BaseDialog;

public class MedicationWarningDialogFragment extends BaseDialog<DialogMedicationWarningBinding> {

    public static final String REQUEST_KEY = "medication_warning_request_key";
    public static final String RESULT_KEY = "medication_warning_result";
    public static final String RESULT_CONTINUE = "continue";
    public static final String RESULT_REVIEW = "review";

    @Override
    protected DialogMedicationWarningBinding inflateBinding(@NonNull LayoutInflater layoutInflater, ViewGroup container) {
        return DialogMedicationWarningBinding.inflate(layoutInflater, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();

        // Set continue button click listener
        binding.btnWarningContinue.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putString(RESULT_KEY, RESULT_CONTINUE);
            dismiss(REQUEST_KEY, result);
        });

        // Set review button click listener
        binding.btnWarningReview.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putString(RESULT_KEY, RESULT_REVIEW);
            dismiss(REQUEST_KEY, result);
        });
    }
} 