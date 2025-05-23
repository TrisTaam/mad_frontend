package com.example.mobile6.presentation.ui.auth.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentAuthLoginBinding
import com.example.mobile6.presentation.ui.MainActivity
import com.example.mobile6.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentAuthLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentAuthLoginBinding
        get() = { inflater, container ->
            FragmentAuthLoginBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Login button click
        binding.btnLogin.setOnClickListener {
            validateAndLogin()
        }

        // Register text click
        binding.tvRegister.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun validateAndLogin() {
        val emailOrPhone = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        viewModel.validateAndLogin(emailOrPhone, password)
    }

    private fun navigateToRegister() {
        // Navigate to registration screen
        navigateTo(R.id.action_loginFragment_to_registerFragment)
    }

    override fun initObservers() {
        // Observe UI messages
        viewModel.uiMessage.onEach { message ->
            (requireActivity() as MainActivity).showToast(message)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        // Observe validation errors
        viewModel.emailError.observe(viewLifecycleOwner) { error ->
            binding.tvEmailValidation.apply {
                text = error
                isVisible = !error.isNullOrEmpty()
            }
        }

        viewModel.passwordError.observe(viewLifecycleOwner) { error ->
            binding.tvPasswordValidation.apply {
                text = error
                isVisible = !error.isNullOrEmpty()
            }
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnLogin.isEnabled = !isLoading
        }

        // Observe login success
        viewModel.loginSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                navigateTo(R.id.action_loginFragment_to_homeFragment)
                viewModel.resetLoginSuccess()
            }
        }
    }
}