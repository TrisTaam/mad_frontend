package com.example.mobile6.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentHomeBinding
import com.example.mobile6.ui.MainActivity
import com.example.mobile6.ui.base.BaseFragment
import com.example.mobile6.ui.util.defaultAnim
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentHomeBinding
        get() = { inflater, container ->
            FragmentHomeBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        binding.btnOpenMedicineSearch.setOnClickListener {
            navigateTo(
                R.id.medicineSearchFragment,
                null,
                NavOptions.Builder().defaultAnim().build()
            )
        }
        binding.btnOpenSpecialty.setOnClickListener {
            navigateTo(
                R.id.doctorSpecialtyFragment,
                null,
                NavOptions.Builder().defaultAnim().build()
            )
        }
        binding.btnTest3.setOnClickListener {
            viewModel.test3()
        }
        binding.btnTestDialog.setOnClickListener {
            navigateTo(R.id.action_homeFragment_to_testDialogFragment)
        }
        binding.btnTestAddPrescription.setOnClickListener {
            navigateTo(
                R.id.prescriptionCreateFragment,
                null,
                NavOptions.Builder().defaultAnim().build()
            )
        }

        binding.btnTestChat.setOnClickListener {
            navigateTo(
                R.id.chatListDoctorFragment
            )
        }

        binding.btnTestChatDoctorToUser.setOnClickListener {
            navigateTo(
                R.id.chatListUserFragment
            )
        }
        binding.btnTestScanPrescription.setOnClickListener {
            navigateTo(R.id.prescriptionScanFragment)
        }
    }

    override fun initObservers() {
        observeBackResult<String>("test") { result ->
            Timber.d("Back result: $result")
        }

        viewModel.testMessage.onEach { testMessage ->
            (requireActivity() as MainActivity).showToast(testMessage)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}