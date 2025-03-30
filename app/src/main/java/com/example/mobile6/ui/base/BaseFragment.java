package com.example.mobile6.ui.base;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import java.util.function.Consumer;

public abstract class BaseFragment<VB extends ViewBinding> extends Fragment {
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        args = getArguments();
        initViews();
    }

    protected void initViews() {

    }

    protected void back() {
        findNavController(this).navigateUp();
    }

    protected void back(String requestKey, Bundle bundle) {
        getParentFragmentManager().setFragmentResult(requestKey, bundle);
        back();
    }

    protected void setOnBackResultListener(String requestKey, Consumer<Bundle> consumer) {
        getParentFragmentManager().setFragmentResultListener(requestKey, this, (requestKey1, result) -> {
            consumer.accept(result);
        });
    }

    protected void navigateTo(int action, @Nullable Bundle bundle) {
        findNavController(this).navigate(action, bundle);
    }

    protected void navigateTo(int action) {
        findNavController(this).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
