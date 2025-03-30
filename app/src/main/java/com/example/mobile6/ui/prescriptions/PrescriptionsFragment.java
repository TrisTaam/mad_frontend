package com.example.mobile6.ui.prescriptions;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.mobile6.databinding.FragmentPrescriptionsBinding;
import com.example.mobile6.ui.base.BaseFragment;


public class PrescriptionsFragment extends BaseFragment<FragmentPrescriptionsBinding> {

    @Override
    protected FragmentPrescriptionsBinding inflateBinding(LayoutInflater layoutInflater, ViewGroup container) {
        return FragmentPrescriptionsBinding.inflate(layoutInflater, container, false);
    }
}