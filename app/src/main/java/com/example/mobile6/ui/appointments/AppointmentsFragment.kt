package com.example.mobile6.ui.appointments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mobile6.databinding.FragmentAppointmentsBinding
import com.example.mobile6.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppointmentsFragment : BaseFragment<FragmentAppointmentsBinding>() {
    private val viewModel: AppointmentsViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentAppointmentsBinding
        get() = { layoutInflater, container ->
            FragmentAppointmentsBinding.inflate(layoutInflater, container, false)
        }

    override fun initViews() {

    }

    override fun initObservers() {

    }
}