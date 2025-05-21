package com.example.mobile6.presentation.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: VB

    protected abstract val bindingInflater: (layoutInflater: LayoutInflater) -> VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
        initViews()
        initObservers()
    }

    abstract fun initViews()

    abstract fun initObservers()

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}