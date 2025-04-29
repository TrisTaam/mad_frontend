package com.example.mobile6.ui.chat

import MessageAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mobile6.databinding.FragmentChatListDoctorBinding
import com.example.mobile6.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.widget.Toast

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

        // Gọi lấy danh sách bác sĩ cho user hiện tại, KHÔNG cần userId nữa
        viewModel.fetchDoctorsForUser()
    }

    override fun initObservers() {
        collectDoctors()
//        collectLoading()
        collectUiMessage()
    }

    private fun collectDoctors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.doctors.collect { doctors ->
                    messageAdapter.submitList(doctors)
                }
            }
        }
    }

//    private fun collectLoading() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.isLoading.collect { isLoading ->
//                    binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//                }
//            }
//        }
//    }

    private fun collectUiMessage() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiMessage.collect { msg ->
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}