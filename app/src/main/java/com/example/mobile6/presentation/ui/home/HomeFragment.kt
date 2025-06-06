package com.example.mobile6.presentation.ui.home

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupWindow
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentHomeBinding
import com.example.mobile6.databinding.ItemAppointmentStatusMenuBinding
import com.example.mobile6.presentation.ui.MainActivity
import com.example.mobile6.presentation.ui.adapter.AppointmentAdapter
import com.example.mobile6.presentation.ui.adapter.HomeUserAlarmAdapter
import com.example.mobile6.presentation.ui.base.BaseFragment
import com.example.mobile6.presentation.ui.util.ReminderScheduler
import com.example.mobile6.presentation.ui.util.gone
import com.example.mobile6.presentation.ui.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.Calendar

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModels()

    private var isFabOpened = false
    private lateinit var fabOpenAnimation: Animation
    private lateinit var fabCloseAnimation: Animation

    private lateinit var appointmentAdapter: AppointmentAdapter
    private lateinit var homeUserAlarmAdapter: HomeUserAlarmAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentHomeBinding
        get() = { inflater, container ->
            FragmentHomeBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        fabOpenAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fabCloseAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
        binding.fabChat.setOnClickListener {
            toggleChatFab()
        }
        binding.fabChatAi.setOnClickListener {
            toggleChatFab()
            navigateTo(R.id.action_homeFragment_to_chatWithAiFragment)
        }
        binding.fabChatUser.setOnClickListener {
            toggleChatFab()
            onNavigateFabChatUser()
        }
        appointmentAdapter = AppointmentAdapter()
        binding.rvAppointment.adapter = appointmentAdapter

        homeUserAlarmAdapter = HomeUserAlarmAdapter { userAlarm ->
            val bundle = bundleOf(
                "prescriptionId" to userAlarm.prescriptionId,
                "isView" to true,
            )
            navigateTo(R.id.action_homeFragment_to_prescriptionDetailScanFragment, bundle)
        }
        binding.rvMedicine.adapter = homeUserAlarmAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.initialize()
    }

    private fun toggleChatFab() {
        if (isFabOpened) {
            binding.fabChatAi.startAnimation(fabCloseAnimation)
            binding.fabChatUser.startAnimation(fabCloseAnimation)
            binding.fabChatAi.gone()
            binding.fabChatUser.gone()
        } else {
            binding.fabChatAi.startAnimation(fabOpenAnimation)
            binding.fabChatUser.startAnimation(fabOpenAnimation)
            binding.fabChatAi.show()
            binding.fabChatUser.show()
        }
        isFabOpened = !isFabOpened
    }

    private var onNavigateFabChatUser = {}

    override fun initObservers() {
        observeBackResult<String>("test") { result ->
            Timber.d("Back result: $result")
        }
        viewModel.uiState.onEach { uiState ->
            if (uiState.isLoading) {
                return@onEach
            }
            if (uiState.error != null) {
                (requireActivity() as MainActivity).showToast(uiState.error)
                return@onEach
            }
            if (uiState.success != null) {
                (requireActivity() as MainActivity).showToast(uiState.success)
                return@onEach
            }
            onNavigateFabChatUser = if (!uiState.isDoctorMode) {
                {
                    navigateTo(R.id.action_homeFragment_to_chatListDoctorFragment)
                }
            } else {
                {
                    navigateTo(R.id.action_homeFragment_to_chatListUserFragment)
                }
            }
            with(binding.llTodayMedicine) {
                if (uiState.isDoctorMode) {
                    gone()
                } else {
                    visible()
                }
            }
            with(binding.btnNotification) {
                if (uiState.isDoctorMode) {
                    gone()
                } else {
                    visible()
                    setOnClickListener {
                        navigateTo(R.id.action_homeFragment_to_notificationFragment)
                    }
                }
            }
            if (uiState.isDoctorMode) {
                appointmentAdapter.onAppointmentClick = { appointment, view, x, y ->
                    if (appointment.status == "PENDING") {
                        val popupWindow = PopupWindow(requireContext())
                        val menuBinding =
                            ItemAppointmentStatusMenuBinding.inflate(
                                LayoutInflater.from(
                                    requireContext()
                                )
                            )

                        popupWindow.contentView = menuBinding.root
                        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
                        popupWindow.elevation = resources.getDimension(R.dimen.elevation_4dp)
                        popupWindow.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                        popupWindow.isFocusable = true

                        menuBinding.tvApprove.setOnClickListener {
                            viewModel.approveAppointment(appointment.id)
                            popupWindow.dismiss()
                        }

                        menuBinding.tvCancel.setOnClickListener {
                            viewModel.cancelAppointment(appointment.id)
                            popupWindow.dismiss()
                        }

                        // Show popup
                        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y)
                    }
                }
            }
            binding.tvUserName.text =
                "${uiState.user.lastName} ${uiState.user.firstName}"
            appointmentAdapter.isDoctorMode = uiState.isDoctorMode
            appointmentAdapter.submitList(uiState.appointments)
            homeUserAlarmAdapter.submitList(uiState.userAlarms)

            uiState.userAlarms.forEach { userAlarm ->
                val hour = userAlarm.notifyTime.split(":")[0].toInt()
                val minute = userAlarm.notifyTime.split(":")[1].toInt()
                scheduleAlarm(
                    (userAlarm.id % Int.MAX_VALUE).toInt(),
                    hour,
                    minute
                )
            }
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun scheduleAlarm(alarmId: Int, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }
        ReminderScheduler.scheduleAlarm(
            requireActivity(),
            calendar,
            alarmId
        )
    }
}