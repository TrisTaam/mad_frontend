package com.example.mobile6.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.mobile6.databinding.FragmentTestDialogBinding;
import com.example.mobile6.ui.base.BaseDialog;

public class TestDialogFragment extends BaseDialog<FragmentTestDialogBinding> {

    @Override
    protected FragmentTestDialogBinding inflateBinding(LayoutInflater layoutInflater, ViewGroup container) {
        return FragmentTestDialogBinding.inflate(layoutInflater, container, false);
    }
}