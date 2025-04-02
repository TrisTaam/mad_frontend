package com.example.mobile6.ui;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mobile6.R;
import com.example.mobile6.databinding.ActivityMainBinding;
import com.example.mobile6.model.Medicine;
import com.example.mobile6.ui.base.BaseActivity;
import com.example.mobile6.ui.medicine.MedicineSearchFragment;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements MedicineSearchFragment.OnMedicineSelectedListener {

    private NavController navController;
    private Button btnOpenMedicineSearch;

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
                        if (btnOpenMedicineSearch != null) {
                            btnOpenMedicineSearch.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.medicineSearchFragment:
                        if (btnOpenMedicineSearch != null) {
                            btnOpenMedicineSearch.setVisibility(View.GONE);
                        }
                        binding.bottomNavigationView.setVisibility(View.GONE);
                        break;
                    default:
                        binding.bottomNavigationView.setVisibility(View.GONE);
                        break;
                }
            });
        }

        btnOpenMedicineSearch = findViewById(R.id.btn_open_medicine_search);
        btnOpenMedicineSearch.setOnClickListener(v -> {
            if (navController != null) {
                navController.navigate(R.id.medicineSearchFragment);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation
                .findNavController(this, R.id.fragment_container_view);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onMedicineSelected(Medicine medicine) {
        if (navController != null) {
            navController.popBackStack();
        }
    }
}