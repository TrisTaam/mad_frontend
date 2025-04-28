package com.example.mobile6.ui.sign_in

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentSignInBinding
import com.example.mobile6.ui.MainActivity
import com.example.mobile6.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    private val viewModel: SignInViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentSignInBinding
        get() = { inflater, container ->
            FragmentSignInBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        binding.btnTestSignIn.setOnClickListener {
            viewModel.signIn("0865231695", "12345678")
        }
    }

    override fun initObservers() {
        viewModel.uiMessage.onEach { uiMessage ->
            (requireActivity() as MainActivity).showToast(uiMessage)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isSuccess.onEach { isSuccess ->
            if (isSuccess) {
                navigateTo(R.id.action_signInFragment_to_homeFragment)
            }
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}