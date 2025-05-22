package com.example.mobile6.presentation.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mobile6.databinding.FragmentNotificationBinding
import com.example.mobile6.presentation.ui.MainActivity
import com.example.mobile6.presentation.ui.adapter.UserAlarmLogAdapter
import com.example.mobile6.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {
    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var adapter: UserAlarmLogAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentNotificationBinding
        get() = { inflater, container ->
            FragmentNotificationBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        binding.backButton.setOnClickListener {
            back()
        }
        adapter = UserAlarmLogAdapter()
        binding.rvUserAlarmLog.adapter = adapter
    }

    override fun initObservers() {
        viewModel.uiState.onEach { uiState ->
            if (uiState.isLoading) {
                return@onEach
            }
            if (uiState.error != null) {
                (requireActivity() as MainActivity).showToast(uiState.error)
                return@onEach
            }
            adapter.submitList(uiState.userAlarmLog)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}