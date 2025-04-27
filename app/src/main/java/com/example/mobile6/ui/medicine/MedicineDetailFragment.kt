package com.example.mobile6.ui.medicine

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobile6.databinding.FragmentMedicineSearchBinding
import com.example.mobile6.ui.base.BaseFragment

class MedicineDetailFragment : BaseFragment<FragmentMedicineSearchBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentMedicineSearchBinding
        get() = { inflater, container ->
            FragmentMedicineSearchBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        // init
    }

    override fun initObservers() {
    }
}