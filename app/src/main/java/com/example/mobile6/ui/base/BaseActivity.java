package com.example.mobile6.ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {
    protected VB binding;

    protected abstract VB inflateBinding();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = inflateBinding();
        setContentView(binding.getRoot());
        initViews();
    }

    protected void initViews() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
