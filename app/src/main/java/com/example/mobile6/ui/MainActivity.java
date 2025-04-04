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

    private NavController navController;

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
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
            navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
                switch (navDestination.getId()) {
                    case R.id.homeFragment:
                    case R.id.statisticsFragment:
                    case R.id.appointmentsFragment:
                    case R.id.prescriptionsFragment:
                    case R.id.profileFragment:
                        binding.bottomNavigationView.setVisibility(View.VISIBLE);
                        binding.btnOpenMedicineSearch.setVisibility(View.VISIBLE);
                        break;
                    case R.id.medicineSearchFragment:
                        binding.btnOpenSpecialty.setVisibility(View.GONE);
                    case R.id.medicineDetailFragment:
                        binding.btnOpenMedicineSearch.setVisibility(View.GONE);
                        binding.bottomNavigationView.setVisibility(View.GONE);
                        break;
                    case R.id.doctorSpecialtyFragment:
                        binding.btnOpenMedicineSearch.setVisibility(View.GONE);
                        binding.btnOpenSpecialty.setVisibility(View.GONE);
                        break;
                    default:
                        binding.bottomNavigationView.setVisibility(View.GONE);
                        break;
                }
            });
        }

        binding.btnOpenMedicineSearch.setOnClickListener(v -> {
            if (navController != null) {
                navController.navigate(R.id.medicineSearchFragment);
            }
        });

        binding.btnOpenSpecialty.setOnClickListener(v -> {
            if (navController != null) {
                navController.navigate(R.id.doctorSpecialtyFragment);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation
                .findNavController(this, R.id.fragment_container_view);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}