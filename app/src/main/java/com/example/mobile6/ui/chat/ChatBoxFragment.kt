package com.example.mobile6.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentChatBoxBinding
import com.example.mobile6.databinding.FragmentChatListDoctorBinding
import com.example.mobile6.ui.base.BaseFragment


class ChatBoxFragment : BaseFragment<FragmentChatBoxBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentChatBoxBinding
        get() = { inflater, container ->
            FragmentChatBoxBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        // Nhận dữ liệu truyền sang từ ChatListDoctorFragment
        val doctorName = arguments?.getString("doctorName")
        // Nếu có id: val doctorId = arguments?.getLong("doctorId")
        // Hiển thị tên bác sĩ trên giao diện (ví dụ)
        binding.tvDoctorName?.text = doctorName
    }

    override fun initObservers() {
    }
}