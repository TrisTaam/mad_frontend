package com.example.mobile6.ui.doctor.doctor_list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobile6.databinding.FragmentDoctorListBinding
import com.example.mobile6.ui.base.BaseFragment

class DoctorListFragment : BaseFragment<FragmentDoctorListBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentDoctorListBinding
        get() = { inflater, container ->
            FragmentDoctorListBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
    }

    override fun initObservers() {
    }
}