package com.example.mobile6.ui.chat

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentChatBoxBinding
import com.example.mobile6.databinding.FragmentChatListDoctorBinding
import com.example.mobile6.ui.adapter.ChatMessageAdapter
import com.example.mobile6.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatBoxFragment : BaseFragment<FragmentChatBoxBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentChatBoxBinding
        get() = { inflater, container ->
            FragmentChatBoxBinding.inflate(inflater, container, false)
        }

    private val viewModel: ChatBoxViewModel by viewModels()
    private lateinit var messageAdapter: ChatMessageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Get doctor ID from arguments
        val doctorId = arguments?.getLong("doctorId")
        if (doctorId == null) {
            Toast.makeText(requireContext(), "Doctor ID not found", Toast.LENGTH_SHORT).show()
            back()
            return
        }
        
        // Set up message adapter
        messageAdapter = ChatMessageAdapter()
        binding.rvChat.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        // Set up send button
        binding.btnSendMessage.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message)
                binding.etMessage.setText("")
            }
        }

        // Set up message sending when Enter is pressed
        binding.etMessage.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val message = binding.etMessage.text.toString().trim()
                if (message.isNotEmpty()) {
                    viewModel.sendMessage(message)
                    binding.etMessage.setText("")
                }
                return@setOnKeyListener true
            }
            false
        }
    }

    override fun initViews() {
        // Get doctor name from arguments
        val doctorName = arguments?.getString("doctorName")
        binding.tvDoctorName?.text = doctorName

        binding.backButton.setOnClickListener {
            back()
        }
    }

    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.messages.collect { messages ->
                    viewModel.currentUserId.collect { userId ->
                        messageAdapter.submitList(messages, userId ?: 0)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiMessage.collect { message ->
                    message?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        viewModel.resetUiMessage()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { isLoading ->
                    binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Get doctor ID from arguments
        val doctorId = arguments?.getLong("doctorId")
        if (doctorId != null) {
            viewModel.setReceiverId(doctorId)
        }
    }
}