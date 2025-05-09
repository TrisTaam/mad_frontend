package com.example.mobile6.ui

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.mobile6.R
import com.example.mobile6.databinding.ActivityMainBinding
import com.example.mobile6.ui.base.BaseActivity
import com.example.mobile6.ui.util.defaultAnim
import com.example.mobile6.ui.util.gone
import com.example.mobile6.ui.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var navController: NavController

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun initViews() {
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