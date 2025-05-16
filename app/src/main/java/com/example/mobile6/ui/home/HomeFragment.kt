package com.example.mobile6.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentHomeBinding
import com.example.mobile6.ui.MainActivity
import com.example.mobile6.ui.base.BaseFragment
import com.example.mobile6.ui.util.gone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModels()

    private var isFabOpened = false
    private lateinit var fabOpenAnimation: Animation
    private lateinit var fabCloseAnimation: Animation

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentHomeBinding
        get() = { inflater, container ->
            FragmentHomeBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        fabOpenAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fabCloseAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
        binding.fabChat.setOnClickListener {
            toggleChatFab()
        }
        binding.fabChatAi.setOnClickListener {
            toggleChatFab()
            navigateTo(R.id.action_homeFragment_to_chatWithAiFragment)
        }
        binding.fabChatUser.setOnClickListener {
            toggleChatFab()
            onNavigateFabChatUser()
        }
    }

    private fun toggleChatFab() {
        if (isFabOpened) {
            binding.fabChatAi.startAnimation(fabCloseAnimation)
            binding.fabChatUser.startAnimation(fabCloseAnimation)
            binding.fabChatAi.gone()
            binding.fabChatUser.gone()
        } else {
            binding.fabChatAi.startAnimation(fabOpenAnimation)
            binding.fabChatUser.startAnimation(fabOpenAnimation)
            binding.fabChatAi.show()
            binding.fabChatUser.show()
        }
        isFabOpened = !isFabOpened
    }

    private var onNavigateFabChatUser = {}

    override fun initObservers() {
        observeBackResult<String>("test") { result ->
            Timber.d("Back result: $result")
        }
        viewModel.uiState.onEach { uiState ->
            if (uiState.isLoading) {
                return@onEach
            }
            if (uiState.error != null) {
                (requireActivity() as MainActivity).showToast(uiState.error)
                return@onEach
            }
            onNavigateFabChatUser = if (!uiState.isDoctorMode) {
                {
                    navigateTo(R.id.action_homeFragment_to_chatListDoctorFragment)
                }
            } else {
                {
                    navigateTo(R.id.action_homeFragment_to_chatListUserFragment)
                }
            }
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}