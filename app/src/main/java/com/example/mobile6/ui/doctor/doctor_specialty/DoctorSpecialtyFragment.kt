package com.example.mobile6.ui.doctor.doctor_specialty

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobile6.databinding.FragmentDoctorSpecialtyBinding
import com.example.mobile6.ui.base.BaseFragment

class DoctorSpecialtyFragment : BaseFragment<FragmentDoctorSpecialtyBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentDoctorSpecialtyBinding
        get() = { inflater, container ->
            FragmentDoctorSpecialtyBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        binding.backButton.setOnClickListener {
            back()
        }
    }

    override fun initObservers() {
    }

}