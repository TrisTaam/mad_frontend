package com.example.mobile6.ui.home;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.mobile6.databinding.FragmentHomeBinding;
import com.example.mobile6.ui.base.BaseFragment;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    @Override
    protected FragmentHomeBinding inflateBinding(LayoutInflater layoutInflater, ViewGroup container) {
        return FragmentHomeBinding.inflate(layoutInflater, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();
    }
}