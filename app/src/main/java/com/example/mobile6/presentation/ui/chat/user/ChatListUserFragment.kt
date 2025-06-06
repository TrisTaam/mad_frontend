package com.example.mobile6.presentation.ui.chat.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentChatListUserBinding
import com.example.mobile6.presentation.ui.adapter.ChatUserAdapter
import com.example.mobile6.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatListUserFragment : BaseFragment<FragmentChatListUserBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentChatListUserBinding
        get() = { inflater, container ->
            FragmentChatListUserBinding.inflate(inflater, container, false)
        }

    private val viewModel: ChatListUserViewModel by viewModels()
    private lateinit var chatUserAdapter: ChatUserAdapter

    override fun initViews() {
        setupRecyclerView()

        binding.backButton.setOnClickListener {
            back()
        }
    }

    private fun setupRecyclerView() {
        chatUserAdapter = ChatUserAdapter { user ->
            val bundle = Bundle().apply {
                putLong("doctorId", user.id)
                putString("doctorName", "${user.firstName} ${user.lastName}")
            }
            navigateTo(
                R.id.action_chatListUserFragment_to_chatBoxFragment,
                bundle
            )
        }
        binding.rvChatUserList.apply {
            adapter = chatUserAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        }
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.search(text.toString())
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
                    chatUserAdapter.submitList(state.users)
                    handleLoadingState(false)
                }
            }
        }
    }

    private fun handleLoadingState(isLoading: Boolean) {
        // Handle loading state UI
    }

    private fun handleErrorState(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }
}