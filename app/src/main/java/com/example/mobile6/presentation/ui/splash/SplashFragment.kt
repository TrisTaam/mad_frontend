package com.example.mobile6.presentation.ui.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentSplashBinding
import com.example.mobile6.presentation.ui.MainActivityViewModel
import com.example.mobile6.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel: SplashViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentSplashBinding
        get() = { inflater, container ->
            FragmentSplashBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
    }

    override fun initObservers() {
        activityViewModel.isLoggedIn.onEach { isLoggedIn ->
            if (isLoggedIn == null) return@onEach
            if (isLoggedIn) {
                viewModel.isRefreshSuccess.onEach { isRefreshSuccess ->
                    if (isRefreshSuccess == null) return@onEach
                    if (isRefreshSuccess) {
                        navigateTo(R.id.action_splashFragment_to_homeFragment)
                    }
                }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}