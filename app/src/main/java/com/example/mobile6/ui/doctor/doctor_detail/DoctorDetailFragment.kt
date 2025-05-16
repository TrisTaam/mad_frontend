package com.example.mobile6.ui.doctor.doctor_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentDoctorDetailBinding
import com.example.mobile6.domain.model.Doctor
import com.example.mobile6.ui.adapter.DoctorDetailAdapter
import com.example.mobile6.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorDetailFragment : BaseFragment<FragmentDoctorDetailBinding>() {

    private lateinit var doctorDetailAdapter: DoctorDetailAdapter
    private var doctor: Doctor? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentDoctorDetailBinding
        get() = { inflater, container ->
            FragmentDoctorDetailBinding.inflate(inflater, container, false)
        }

    override fun processArguments(args: Bundle) {
        doctor = arguments?.getParcelable("doctor")
    }

    override fun initViews() {
        binding.backButton.setOnClickListener {
            back()
        }

        binding.appointmentButton.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("doctor", doctor)
            }
            navigateTo(R.id.action_doctorDetailFragment_to_doctorAppointmentsFragment, bundle)
        }

        doctor?.let { doctor ->
            binding.apply {
                Glide.with(requireContext())
                    .load(doctor.avatar)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(doctorAvatar)
                doctorName.text = "${doctor.lastName} ${doctor.firstName}"
                doctorSpecialty.text = doctor.specialty
            }
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        doctorDetailAdapter = DoctorDetailAdapter { }

        binding.doctorDescriptionRecyclerView.apply {
            adapter = doctorDetailAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        doctor?.doctorInfos?.let { doctorInfos ->
            doctorDetailAdapter.submitList(doctorInfos.sortedBy { it.order })
        }
    }

    override fun initObservers() {
    }
}