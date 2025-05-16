package com.example.mobile6.ui.chat.ai

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile6.databinding.FragmentChatWithAiBinding
import com.example.mobile6.domain.model.ChatAIMessage
import com.example.mobile6.ui.adapter.ChatAIAdapter
import com.example.mobile6.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatWithAiFragment : BaseFragment<FragmentChatWithAiBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentChatWithAiBinding
        get() = { inflater, container ->
            FragmentChatWithAiBinding.inflate(inflater, container, false)
        }

    private val viewModel: ChatWithAiViewModel by viewModels()
    private lateinit var messageAdapter: ChatAIAdapter

    override fun initViews() {
        setupToolbar()
        setupRecyclerView()
        setupMessageInput()
    }

    private fun setupToolbar() {
        binding.backButton.setOnClickListener {
            back()
        }
    }

    private fun setupRecyclerView() {
        messageAdapter = ChatAIAdapter()
        binding.messagesRecyclerView.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
        }
    }

    private fun setupMessageInput() {
        binding.messageInput.addTextChangedListener { text ->
            binding.sendMessageBtn.isEnabled = !text.isNullOrBlank()
        }

        binding.sendMessageBtn.setOnClickListener {
            val content = binding.messageInput.text.toString().trim()
            if (content.isNotEmpty()) {
                viewModel.sendMessage(content)
                binding.messageInput.text.clear()
                binding.sendMessageBtn.isEnabled = false
            }
        }
    }

    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    if (state.isSending) {
                        handleLoadingState(true)
                        return@collectLatest
                    }
                    if (state.error != null) {
                        handleErrorState(state.error)
                        return@collectLatest
                    }
                    messageAdapter.submitList(state.messages.map { content ->
                        ChatAIMessage(
                            content = content,
                            isUser = content.startsWith("You: ")
                        )
                    })
                    binding.messagesRecyclerView.scrollToPosition(state.messages.size - 1)
                    handleLoadingState(false)
                }
            }
        }
    }

    private fun handleLoadingState(isLoading: Boolean) {
        // You can add loading state handling here if needed
    }

    private fun handleErrorState(error: String?) {
        error?.let {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }
}