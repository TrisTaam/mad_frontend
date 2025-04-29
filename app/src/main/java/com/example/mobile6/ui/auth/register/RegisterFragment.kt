package com.example.mobile6.ui.auth.register
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentAuthRegisterBinding
import com.example.mobile6.ui.MainActivity
import com.example.mobile6.ui.base.BaseFragment
import com.example.mobile6.ui.util.defaultAnim
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentAuthRegisterBinding>() {
    private val viewModel: RegisterViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentAuthRegisterBinding
        get() = { inflater, container ->
            FragmentAuthRegisterBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Register button click
        binding.btnRegister.setOnClickListener {
            validateAndRegister()
        }
        // Login text click
        binding.tvLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun validateAndRegister() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val email = binding.etEmail.text.toString()
        val phone = binding.etPhone.text.toString()
        val password = binding.etPassword.text.toString()

        viewModel.validateAndRegister(firstName, lastName, email, phone, password)
    }

    private fun navigateToLogin() {
        // Navigate to login screen
        navigateTo(R.id.action_registerFragment_to_loginFragment)
    }

    override fun initObservers() {
        // Observe UI messages
        viewModel.uiMessage.onEach { message ->
            (requireActivity() as MainActivity).showToast(message)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        // Observe validation errors
        viewModel.firstNameError.observe(viewLifecycleOwner) { error ->
            binding.tvFirstNameValidation.apply {
                text = error
                isVisible = !error.isNullOrEmpty()
            }
        }

        viewModel.lastNameError.observe(viewLifecycleOwner) { error ->
            binding.tvLastNameValidation.apply {
                text = error
                isVisible = !error.isNullOrEmpty()
            }
        }

        viewModel.emailError.observe(viewLifecycleOwner) { error ->
            binding.tvEmailValidation.apply {
                text = error
                isVisible = !error.isNullOrEmpty()
            }
        }

        viewModel.phoneError.observe(viewLifecycleOwner) { error ->
            binding.tvPhoneValidation.apply {
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
            binding.btnRegister.isEnabled = !isLoading
        }

        // Observe registration success
        viewModel.registrationSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                navigateTo(R.id.action_registerFragment_to_profileCompletionFragment)
                viewModel.resetRegistrationSuccess()
            }
        }
    }
}