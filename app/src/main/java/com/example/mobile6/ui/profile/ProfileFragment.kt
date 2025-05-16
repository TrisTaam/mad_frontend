package com.example.mobile6.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentProfileBinding
import com.example.mobile6.ui.MainActivity
import com.example.mobile6.ui.base.BaseFragment
import com.example.mobile6.ui.util.gone
import com.example.mobile6.ui.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val viewModel: ProfileViewModel by viewModels()

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentProfileBinding
        get() = { inflater, container ->
            FragmentProfileBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        binding.btnSignOut.setOnClickListener {
            viewModel.signOut()
        }
        binding.backButton.setOnClickListener {
            back()
        }
        binding.btnChangeMode.setOnClickListener {
            changeMode()
        }
        binding.btnEdit.setOnClickListener {
            val bundle = bundleOf(
                "isEditMode" to true,
                "gender" to viewModel.uiState.value.user.gender,
                "dateOfBirth" to viewModel.uiState.value.user.dateOfBirth?.format(
                    DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                    )
                ),
                "weight" to viewModel.uiState.value.user.weight,
                "height" to viewModel.uiState.value.user.height
            )
            navigateTo(R.id.action_profileFragment_to_profileCompletionFragment, bundle)
        }
    }

    override fun initObservers() {
        observeBackResult<Boolean>("updateProfile") { _ ->
            viewModel.getUserDetail()
        }

        viewModel.uiState.onEach { uiState ->
            if (uiState.isLoading) {
                binding.clInfo.gone()
                return@onEach
            }
            if (uiState.errorMessage != null) {
                (requireActivity() as MainActivity).showToast(uiState.errorMessage)
                return@onEach
            }
            binding.clInfo.visible()
            binding.tvName.text = "${uiState.user.lastName} ${uiState.user.firstName}"
            binding.tvDob.text =
                uiState.user.dateOfBirth?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "N/A"
            binding.tvHeightInfo.text = uiState.user.height?.let {
                "${it}cm"
            } ?: "N/A"
            binding.tvWeightInfo.text = uiState.user.weight?.let {
                "${it}kg"
            } ?: "N/A"

            val age = uiState.user.dateOfBirth?.let { userDate ->
                val currentDate = LocalDate.now()
                val age = currentDate.year - userDate.year
                if (currentDate.monthValue < userDate.monthValue || (currentDate.monthValue == userDate.monthValue && currentDate.dayOfMonth < userDate.dayOfMonth)) {
                    age - 1
                } else {
                    age
                }
            } ?: "N/A"
            binding.tvAgeInfo.text = "$age"
            Glide.with(requireContext())
                .load(uiState.user.avatar)
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(binding.ivAvatar)

            uiState.isDoctorMode?.let {
                binding.btnChangeMode.apply {
                    text = "Chuyển sang chế độ ${if (it) "bệnh nhân" else "bác sĩ"}"
                    visible()
                }
            } ?: run {
                binding.btnChangeMode.gone()
            }
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun changeMode() {
        viewModel.changeMode()
        navigateTo(R.id.action_profileFragment_to_splashFragment)
    }
}