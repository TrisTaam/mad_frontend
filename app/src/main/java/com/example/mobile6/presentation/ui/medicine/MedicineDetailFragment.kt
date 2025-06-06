package com.example.mobile6.presentation.ui.medicine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentMedicineDetailBinding
import com.example.mobile6.domain.model.Medicine
import com.example.mobile6.domain.model.MedicineInteraction
import com.example.mobile6.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MedicineDetailFragment : BaseFragment<FragmentMedicineDetailBinding>() {

    private val viewModel: MedicineDetailViewModel by viewModels();
    private var medicineId: Long = 0L
    private var usedMedicineIds: List<Long> = listOf()

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentMedicineDetailBinding
        get() = { inflater, container ->
            FragmentMedicineDetailBinding.inflate(inflater, container, false)
        }

    override fun processArguments(args: Bundle) {
        medicineId = arguments?.getLong("medicineId") ?: 0L
        if (viewModel.uiState.value.medicine == null) {
            viewModel.fetchMedicineDetail(medicineId)
        }
    }

    override fun initViews() {
        binding.backButton.setOnClickListener {
            back()
        }

        parentFragmentManager.setFragmentResultListener(
            "prescriptionData",
            viewLifecycleOwner
        ) { _, bundle ->
            val usedMedicineIdStrings = bundle.getStringArrayList("usedMedicineIds")
            usedMedicineIds = usedMedicineIdStrings?.map { it.toLong() } ?: listOf()
        }

        binding.addToPrescriptionButton.setOnClickListener {
            viewModel.checkMedicineInteractions(medicineId, usedMedicineIds)
        }
    }

    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    if (state.isLoading) {
                        handleLoadingState(true)
                        return@collectLatest
                    }
                    if (state.error != null) {
                        handleErrorState(state.error)
                        return@collectLatest
                    }
                    state.medicine?.let {
                        bindMedicineData(it)
                    }
                    state.medicineInteraction?.let {
                        handleMedicineInteraction(it)
                    }
                }
            }
        }
    }

    private fun handleMedicineInteraction(interaction: MedicineInteraction) {
        if (!interaction.hasInteraction) {
            Timber.d("không có tương tác thuốc muinv")
            val bundle = Bundle().apply {
                putSerializable("selectedMedicine", viewModel.uiState.value.medicine)
            }
            parentFragmentManager.setFragmentResult("prescriptionDataSelectMedicine", bundle)
            back()
            back()
            return
        }

        val bundle = Bundle().apply {
            putLong("medicineId", medicineId)
            putString("interactionMessage", interaction.interactionDetails)
        }
        navigateTo(
            R.id.action_medicineDetailFragment_to_medicationWarningDialogFragment,
            bundle
        )
    }

    private fun bindMedicineData(medicine: Medicine) {
        binding.apply {
            Glide.with(requireContext())
                .load(medicine.imgUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(medicineImage)

            medicineDescription.text = medicine.description
            medicineDirection.text = medicine.usage
            medicineWarning.text = medicine.precaution
        }
    }


    private fun handleLoadingState(isLoading: Boolean) {
    }

    private fun handleErrorState(error: String?) {
    }
}