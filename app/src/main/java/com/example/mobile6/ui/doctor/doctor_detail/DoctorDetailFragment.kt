package com.example.mobile6.ui.doctor.doctor_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobile6.databinding.FragmentDoctorDetailBinding
import com.example.mobile6.ui.base.BaseFragment

class DoctorDetailFragment : BaseFragment<FragmentDoctorDetailBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentDoctorDetailBinding
        get() = { inflater, container ->
            FragmentDoctorDetailBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
    }

    override fun initObservers() {
    }

}