package com.example.mobile6.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mobile6.databinding.FragmentProfileBinding
import com.example.mobile6.ui.MainActivity
import com.example.mobile6.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val viewModel: ProfileViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentProfileBinding
        get() = { inflater, container ->
            FragmentProfileBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        binding.btnSignOut.setOnClickListener {
            viewModel.signOut()
        }
    }

    override fun initObservers() {
        viewModel.uiMessage.onEach { uiMessage ->
            (requireActivity() as MainActivity).showToast(uiMessage)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}