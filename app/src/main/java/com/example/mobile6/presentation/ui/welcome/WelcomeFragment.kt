package com.example.mobile6.presentation.ui.welcome

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentWelcomeBinding
import com.example.mobile6.presentation.ui.base.BaseFragment

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentWelcomeBinding
        get() = { layoutInflater, viewGroup ->
            FragmentWelcomeBinding.inflate(layoutInflater, viewGroup, false)
        }

    override fun initViews() {
        binding.btnStart.setOnClickListener {
            navigateTo(R.id.action_welcomeFragment_to_loginFragment)
        }
    }

    override fun initObservers() {

    }
}