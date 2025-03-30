package com.example.mobile6.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewbinding.ViewBinding;

import com.example.mobile6.R;

public abstract class BaseDialog<VB extends ViewBinding> extends DialogFragment {
    protected VB binding;
    protected Bundle args;

    protected abstract VB inflateBinding(LayoutInflater layoutInflater, ViewGroup container);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = inflateBinding(inflater, container);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.rounded_dialog_style);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        args = getArguments();
        initViews();
    }

    protected void initViews() {

    }

    protected void dismiss(String requestKey, Bundle bundle) {
        getParentFragmentManager().setFragmentResult(requestKey, bundle);
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
