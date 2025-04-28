package com.example.mobile6.ui.chat

import MessageAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.mobile6.databinding.FragmentChatListDoctorBinding
import com.example.mobile6.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListDoctorFragment : BaseFragment<FragmentChatListDoctorBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentChatListDoctorBinding
        get() = { inflater, container ->
            FragmentChatListDoctorBinding.inflate(inflater, container, false)
        }

    private val viewModel: ChatListDoctorViewModel by viewModels()
    private lateinit var messageAdapter: MessageAdapter

    override fun initViews() {
        messageAdapter = MessageAdapter { doctor ->
            // Xử lý khi chọn bác sĩ (ví dụ: mở chat)
        }
        binding.rvDoctorList.adapter = messageAdapter

        // Gọi lấy danh sách bác sĩ cho user hiện tại
        val userId = getCurrentUserId() // TODO: thay bằng cách lấy userId thực tế
        viewModel.fetchDoctorsForUser(userId)
    }

    override fun initObservers() {
        viewModel.doctors.observe(viewLifecycleOwner, Observer { doctors ->
            messageAdapter.submitList(doctors)
        })
    }

    private fun getCurrentUserId(): Long {
        // TODO: Lấy userId thực tế từ SharedPreferences, SessionManager hoặc AuthManager
        return 1L // Giá trị mẫu
    }
}