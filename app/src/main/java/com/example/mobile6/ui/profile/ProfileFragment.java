package com.example.mobile6.ui.profile;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.mobile6.databinding.FragmentProfileBinding;
import com.example.mobile6.ui.base.BaseFragment;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding> {


    @Override
    protected FragmentProfileBinding inflateBinding(LayoutInflater layoutInflater, ViewGroup container) {
        return FragmentProfileBinding.inflate(layoutInflater, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();
    }
}