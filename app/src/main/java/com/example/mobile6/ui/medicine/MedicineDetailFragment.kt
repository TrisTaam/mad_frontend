package com.example.mobile6.ui.medicine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import com.bumptech.glide.Glide
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentMedicineDetailBinding
import com.example.mobile6.domain.model.Medicine
import com.example.mobile6.ui.adapter.MedicineAdapter
import com.example.mobile6.ui.base.BaseFragment
import com.example.mobile6.ui.util.defaultAnim
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MedicineDetailFragment : BaseFragment<FragmentMedicineDetailBinding>() {

    private val viewModel: MedicineDetailViewModel by viewModels();
    private lateinit var medicineAdapter: MedicineAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentMedicineDetailBinding
        get() = { inflater, container ->
            FragmentMedicineDetailBinding.inflate(inflater, container, false)
        }

    override fun processArguments(args: Bundle) {
        val medicineId = arguments?.getLong("medicineId") ?: 0L
        viewModel.fetchMedicineDetail(medicineId)
    }

    override fun initViews() {
        binding.backButton.setOnClickListener {
            back()
        }

        binding.addToPrescriptionButton.setOnClickListener {
            navigateTo(
                R.id.action_medicineDetailFragment_to_medicationWarningDialogFragment,
                null,
                NavOptions.Builder().defaultAnim().build()
            )
        }


    }

    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state->
                    handleLoadingState(state.isLoading)
                    handleErrorState(state.error)
                    state.medicine?.let{
                        bindMedicineData(it)
                    }
                }
            }
        }
    }

    private fun bindMedicineData(medicine: Medicine) {
        binding.apply {
            Glide.with(requireContext())
                .load(medicine.imgUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(medicineImage)

            medicineDescription.text = medicine.description
            medicineUsage.text = medicine.use
            medicineDirection.text = medicine.usage
            medicineWarning.text = medicine.precaution
        }
    }


    private fun handleLoadingState(isLoading: Boolean) {
        // Show/hide loading indicator if you have one
        // binding.progressBar.isVisible = isLoading
    }

    private fun handleErrorState(error: String?) {
        // Show error message if needed
        // error?.let {
        //     Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        // }
    }
}