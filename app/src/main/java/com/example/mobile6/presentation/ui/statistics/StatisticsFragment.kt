package com.example.mobile6.presentation.ui.statistics

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobile6.databinding.FragmentStatisticsBinding
import com.example.mobile6.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentStatisticsBinding
        get() = { layoutInflater, container ->
            FragmentStatisticsBinding.inflate(layoutInflater, container, false)
        }

    override fun initViews() {

    }

    override fun initObservers() {

    }

}