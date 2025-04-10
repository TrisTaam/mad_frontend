package com.example.mobile6.ui.prescriptions

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobile6.databinding.FragmentPrescriptionsBinding
import com.example.mobile6.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrescriptionsFragment : BaseFragment<FragmentPrescriptionsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentPrescriptionsBinding
        get() = { inflater, container ->
            FragmentPrescriptionsBinding.inflate(inflater, container, false)
        }

    override fun initViews() {

    }

    override fun initObservers() {

    }

}