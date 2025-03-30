package com.example.mobile6.ui;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mobile6.R;
import com.example.mobile6.databinding.ActivityMainBinding;
import com.example.mobile6.ui.base.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected ActivityMainBinding inflateBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void initViews() {
        super.initViews();
        binding.bottomNavigationView.setItemIconTintList(null);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container_view);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
            navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
                switch (navDestination.getId()) {
                    case R.id.homeFragment:
                    case R.id.statisticsFragment:
                    case R.id.appointmentsFragment:
                    case R.id.prescriptionsFragment:
                    case R.id.profileFragment:
                        binding.bottomNavigationView.setVisibility(View.VISIBLE);
                        break;
                    default:
                        binding.bottomNavigationView.setVisibility(View.GONE);
                        break;
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation
                .findNavController(this, R.id.fragment_container_view);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}