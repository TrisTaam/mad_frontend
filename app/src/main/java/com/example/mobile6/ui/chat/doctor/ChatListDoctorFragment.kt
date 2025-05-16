package com.example.mobile6.ui.chat.doctor

import MessageAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentChatListDoctorBinding
import com.example.mobile6.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatListDoctorFragment : BaseFragment<FragmentChatListDoctorBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentChatListDoctorBinding
        get() = { inflater, container ->
            FragmentChatListDoctorBinding.inflate(inflater, container, false)
        }

    private val viewModel: ChatListDoctorViewModel by viewModels()
    private lateinit var messageAdapter: MessageAdapter

    override fun initViews() {
        setupRecyclerView()

        binding.backButton.setOnClickListener {
            back()
        }
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter { doctor ->
            val bundle = Bundle().apply {
                putLong("doctorId", doctor.id)
                putString("doctorName", "${doctor.firstName} ${doctor.lastName}")
            }
            navigateTo(
                R.id.action_chatListDoctorFragment_to_chatBoxFragment,
                bundle
            )
        }
        binding.rvDoctorList.apply {
            adapter = messageAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        }
    }

    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    if (state.isLoading) {
                        handleLoadingState(true)
                        return@collectLatest
                    }
                    if (state.error != null) {
                        handleErrorState(state.error)
                        return@collectLatest
                    }
                    messageAdapter.submitList(state.doctors)
                    handleLoadingState(false)
                }
            }
        }
    }

    private fun handleLoadingState(isLoading: Boolean) {
//        binding.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun handleErrorState(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }
}