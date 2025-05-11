package com.example.mobile6.ui.prescriptions

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobile6.databinding.FragmentPrescriptionDetailBinding
import com.example.mobile6.ui.base.BaseFragment

class PrescriptionDetailFragment : BaseFragment<FragmentPrescriptionDetailBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentPrescriptionDetailBinding
        get() = { inflater, container ->
            FragmentPrescriptionDetailBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
    }

    override fun initObservers() {
    }
}