package com.example.mobile6.presentation.ui

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.mobile6.R
import com.example.mobile6.databinding.ActivityMainBinding
import com.example.mobile6.presentation.ui.base.BaseActivity
import com.example.mobile6.presentation.ui.util.defaultAnim
import com.example.mobile6.presentation.ui.util.gone
import com.example.mobile6.presentation.ui.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var navController: NavController

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                showToast("Cần quyền gửi thông báo để sử dụng tính năng nhắc nhở")
            }
        }

    override fun initViews() {
        checkNotificationPermission()
        checkExactAlarmPermission()
        binding.bottomNavigationView.itemIconTintList = null
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        setupWithNavController(binding.bottomNavigationView, navController)
        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            when (navDestination.id) {
                R.id.homeFragment,
                R.id.statisticsFragment,
                R.id.appointmentsFragment,
                R.id.prescriptionsFragment,
                R.id.profileFragment -> binding.bottomNavigationView.visible()

                else -> binding.bottomNavigationView.gone()
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }
    }

    override fun initObservers() {
        viewModel.isLoggedIn.onEach { isLoggedIn ->
            if (isLoggedIn == null) return@onEach
            if (!isLoggedIn) {
                navController.navigate(
                    R.id.welcomeFragment,
                    null,
                    NavOptions.Builder()
                        .defaultAnim()
                        .setPopUpTo(R.id.nav_graph, true)
                        .build()
                )
            }
        }.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .launchIn(lifecycleScope)
    }
}