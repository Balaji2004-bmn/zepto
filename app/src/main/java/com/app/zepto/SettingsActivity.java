package com.app.zepto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    private TextView tvUserName, tvUserEmail;
    private Button btnEditProfile, btnManageAddresses, btnChangePassword, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeViews();
        setupClickListeners();
        loadUserData();
        setupBottomNavigation(); // FIXED: Now has null check
    }

    private void initializeViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnManageAddresses = findViewById(R.id.btnManageAddresses);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void setupClickListeners() {
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        btnManageAddresses.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AddressActivity.class);
            startActivity(intent);
        });

        btnChangePassword.setOnClickListener(v -> {
            // Implement change password functionality
            // Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
            // startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            logoutUser();
        });
    }

    private void loadUserData() {
        // Load user data from SharedPreferences or database
        // Example:
        // String userName = prefs.getString("user_name", "User");
        // String userEmail = prefs.getString("user_email", "user@example.com");

        tvUserName.setText("John Doe"); // Replace with actual user name
        tvUserEmail.setText("john.doe@example.com"); // Replace with actual user email
    }

    private void logoutUser() {
        // Clear user session
        android.content.SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        // Navigate to login screen
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // FIXED: Added null check for bottom navigation
    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Add null check to prevent crash
        if (bottomNavigationView == null) {
            return; // Exit if bottom navigation is not present
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, ProductPage.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_categories) {
                startActivity(new Intent(this, CategoryActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_orders) {
                startActivity(new Intent(this, OrdersActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Already on Settings/Profile page
                return true;
            }
            return false;
        });

        // Set current item as selected
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}