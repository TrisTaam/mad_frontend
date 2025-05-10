package com.example.mobile6.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile6.databinding.FragmentChatBoxBinding
import com.example.mobile6.ui.adapter.ChatMessageAdapter
import com.example.mobile6.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatBoxFragment : BaseFragment<FragmentChatBoxBinding>() {

    private val viewModel: ChatBoxViewModel by viewModels()
    private lateinit var messageAdapter: ChatMessageAdapter
    private var doctorId: Long = 0
    private var doctorName: String = ""

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentChatBoxBinding
        get() = { inflater, container ->
            FragmentChatBoxBinding.inflate(inflater, container, false)
        }

    override fun processArguments(args: Bundle) {
        doctorId = args.getLong("doctorId")
        doctorName = args.getString("doctorName", "")
        
        // Load conversation when fragment is created
        if (viewModel.uiState.value.messages.isEmpty()) {
            viewModel.getConversation(doctorId)
        }
    }

    override fun initViews() {
        setupToolbar()
        setupRecyclerView()
        setupMessageInput()
    }

    private fun setupToolbar() {
        binding.doctorName.text = doctorName
        binding.backButton.setOnClickListener {
            back()
        }
    }

    private fun setupRecyclerView() {
        messageAdapter = ChatMessageAdapter()
        binding.messagesRecyclerView.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
        }
    }

    private fun setupMessageInput() {
        // Ẩn nút gửi ảnh vì chưa implement
        binding.sendImgBtn.visibility = View.GONE
        
        // Enable/disable nút gửi dựa trên nội dung input
        binding.messageInput.addTextChangedListener { text ->
            binding.sendMessageBtn.isEnabled = !text.isNullOrBlank()
        }
        
        binding.sendMessageBtn.setOnClickListener {
            val content = binding.messageInput.text.toString().trim()
            if (content.isNotEmpty()) {
                viewModel.sendMessage(doctorId, content)
                binding.messageInput.text.clear()
                binding.sendMessageBtn.isEnabled = false
            }
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
                    messageAdapter.submitList(state.messages)
                    binding.messagesRecyclerView.scrollToPosition(state.messages.size - 1)
                    handleLoadingState(false)
                    
                    // Disable input khi đang gửi tin nhắn
                    binding.messageInput.isEnabled = !state.isSending
                    binding.sendMessageBtn.isEnabled = !state.isSending && !binding.messageInput.text.isNullOrBlank()
                }
            }
        }
    }

    private fun handleLoadingState(isLoading: Boolean) {
        binding.messagesRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun handleErrorState(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }
} 