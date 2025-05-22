package com.example.mobile6.presentation.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentProfileCompletionBinding
import com.example.mobile6.presentation.ui.MainActivity
import com.example.mobile6.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ProfileCompletionFragment : BaseFragment<FragmentProfileCompletionBinding>() {
    private val viewModel: ProfileCompletionViewModel by viewModels()

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private var isEditMode = false

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentProfileCompletionBinding
        get() = { inflater, container ->
            FragmentProfileCompletionBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        setupClickListeners()
        observeProfileData()
    }

    override fun processArguments(args: Bundle) {
        super.processArguments(args)
        isEditMode = args.getBoolean("isEditMode", false)
        if (isEditMode) {
            args.getString("gender")?.let {
                viewModel.setGender(it)
            }
            args.getString("dateOfBirth")?.let {
                dateFormatter.parse(it)?.let {
                    viewModel.setDateOfBirth(it)
                }
            }
            args.getInt("weight").let {
                binding.etWeight.setText(it.toString())
            }
            args.getInt("height").let {
                binding.etHeight.setText(it.toString())
            }
        }
    }

    private fun setupClickListeners() {
        // Gender selection
        binding.cvGender.setOnClickListener {
            showGenderSelectionDialog()
        }
        // Date of birth selection
        binding.cvDateOfBirth.setOnClickListener {
            showDatePickerDialog()
        }
        // Complete button
        binding.btnComplete.setOnClickListener {
            validateAndSaveProfile()
        }
    }

    private fun observeProfileData() {
        // Observe gender changes
        viewModel.gender.observe(viewLifecycleOwner) { gender ->
            if (gender.isNotEmpty()) {
                binding.tvGender.text = gender
                binding.tvGender.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.black
                    )
                )
            }
        }

        // Observe date of birth changes
        viewModel.dateOfBirth.observe(viewLifecycleOwner) { date ->
            if (date != null) {
                val formattedDate = dateFormatter.format(date)
                binding.tvDateOfBirth.text = formattedDate
                binding.tvDateOfBirth.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.black
                    )
                )
            }
        }

    }

    private fun showGenderSelectionDialog() {
        val bundle = Bundle().apply {
            putString("currentGender", viewModel.gender.value)
        }
        navigateTo(R.id.action_profileCompletionFragment_to_genderSelectDialogFragment, bundle)
    }

    private fun showDatePickerDialog() {
        val calendar = if (viewModel.dateOfBirth.value != null) {
            Calendar.getInstance().apply {
                timeInMillis = viewModel.dateOfBirth.value?.time!!
            }
        } else {
            Calendar.getInstance()
        }
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                viewModel.setDateOfBirth(selectedCalendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Set max date to current date (no future dates)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun validateAndSaveProfile() {
        val weight = binding.etWeight.text.toString()
        val height = binding.etHeight.text.toString()
        viewModel.validateAndSaveProfile(weight, height)
    }

    override fun initObservers() {
        // Observe UI messages from ViewModel
        viewModel.uiMessage.onEach { uiMessage ->
            (requireActivity() as MainActivity).showToast(uiMessage)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        // Observe validation errors
        viewModel.weightError.observe(viewLifecycleOwner) { errorMsg ->
            binding.etWeight.error = errorMsg
        }

        viewModel.heightError.observe(viewLifecycleOwner) { errorMsg ->
            binding.etHeight.error = errorMsg
        }

        // Observe navigation events
        viewModel.navigateToNextScreen.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                // Navigate to next screen
                // findNavController().navigate(R.id.action_profileCompletionFragment_to_nextScreen)
                if (isEditMode) {
                    back("updateProfile", true)
                } else {
                    back()
                    back()
                }
                viewModel.onNavigationComplete()
            }
        }
    }
}