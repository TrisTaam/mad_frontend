package com.example.mobile6.ui.appointment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.mobile6.databinding.FragmentAppointmentsBinding;
import com.example.mobile6.ui.base.BaseFragment;

public class AppointmentsFragment extends BaseFragment<FragmentAppointmentsBinding> {


    @Override
    protected FragmentAppointmentsBinding inflateBinding(LayoutInflater layoutInflater, ViewGroup container) {
        return FragmentAppointmentsBinding.inflate(layoutInflater, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}