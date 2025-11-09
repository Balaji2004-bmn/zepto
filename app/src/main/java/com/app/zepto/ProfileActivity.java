package com.app.zepto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName, tvUserEmail, tvUserPhone, tvDefaultAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeViews();
        loadUserData();
        setupClickListeners();
        setupBottomNavigation();
    }

    private void initializeViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserPhone = findViewById(R.id.tvUserPhone);
        tvDefaultAddress = findViewById(R.id.tvDefaultAddress);
    }

    private void loadUserData() {
        // Load user profile data
        android.content.SharedPreferences profilePrefs = getSharedPreferences("user_profile", MODE_PRIVATE);
        String userName = profilePrefs.getString("user_name", "John Doe");
        String userEmail = profilePrefs.getString("user_email", "john.doe@example.com");
        String userPhone = profilePrefs.getString("user_phone", "+91 9876543210");

        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);
        tvUserPhone.setText(userPhone);

        // Load default address to show in profile
        android.content.SharedPreferences addressPrefs = getSharedPreferences("user_addresses", MODE_PRIVATE);
        String addressesJson = addressPrefs.getString("address_list", "[]");

        Gson gson = new Gson();
        Type type = new TypeToken<List<Address>>() {}.getType();
        List<Address> addresses = gson.fromJson(addressesJson, type);

        if (addresses != null && !addresses.isEmpty()) {
            // Find default address or use first one
            Address defaultAddress = null;
            for (Address address : addresses) {
                if (address.isDefault()) {
                    defaultAddress = address;
                    break;
                }
            }
            if (defaultAddress == null && !addresses.isEmpty()) {
                defaultAddress = addresses.get(0);
            }

            if (defaultAddress != null) {
                tvDefaultAddress.setText(defaultAddress.getCompleteAddress());
            } else {
                tvDefaultAddress.setText("No address saved. Tap to add address.");
            }
        } else {
            tvDefaultAddress.setText("No address saved. Tap to add address.");
        }
    }

    private void setupClickListeners() {
        // Profile sections
        findViewById(R.id.layoutEditProfile).setOnClickListener(v -> {
            editProfile();
        });

        findViewById(R.id.layoutAddress).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AddressActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.layoutOrders).setOnClickListener(v -> {
            startActivity(new Intent(this, OrdersActivity.class));
        });

        findViewById(R.id.layoutSettings).setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });

        findViewById(R.id.layoutHelpSupport).setOnClickListener(v -> {
            showHelpSupport();
        });

        findViewById(R.id.layoutAbout).setOnClickListener(v -> {
            showAbout();
        });

        // Logout button
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            logout();
        });

        // Edit profile icon
        findViewById(R.id.ivEditProfile).setOnClickListener(v -> {
            editProfile();
        });
    }

    private void editProfile() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Edit Profile");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);
        builder.setView(dialogView);

        android.widget.EditText etName = dialogView.findViewById(R.id.etName);
        android.widget.EditText etEmail = dialogView.findViewById(R.id.etEmail);
        android.widget.EditText etPhone = dialogView.findViewById(R.id.etPhone);

        // Pre-fill current data
        etName.setText(tvUserName.getText().toString());
        etEmail.setText(tvUserEmail.getText().toString());
        etPhone.setText(tvUserPhone.getText().toString());

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = etName.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();
            String newPhone = etPhone.getText().toString().trim();

            if (newName.isEmpty()) {
                android.widget.Toast.makeText(this, "Please enter name", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            // Save to SharedPreferences
            android.content.SharedPreferences prefs = getSharedPreferences("user_profile", MODE_PRIVATE);
            android.content.SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user_name", newName);
            editor.putString("user_email", newEmail);
            editor.putString("user_phone", newPhone);
            editor.apply();

            // Update UI
            tvUserName.setText(newName);
            tvUserEmail.setText(newEmail);
            tvUserPhone.setText(newPhone);

            android.widget.Toast.makeText(this, "Profile updated successfully!", android.widget.Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showHelpSupport() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Help & Support")
                .setMessage("For any queries or support:\n\n" +
                        "📞 Call: 1800-123-4567\n" +
                        "📧 Email: support@zepto.com\n" +
                        "💬 Live Chat: Available 24/7\n\n" +
                        "Our customer support team is always ready to help you!")
                .setPositiveButton("OK", null)
                .setNeutralButton("Call Support", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(android.net.Uri.parse("tel:18001234567"));
                    startActivity(intent);
                })
                .show();
    }

    private void showAbout() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("About Zepto")
                .setMessage("Zepto - Grocery Delivery App\n\n" +
                        "Version: 1.0.0\n" +
                        "Build: 2024.01.01\n\n" +
                        "Zepto delivers groceries and essentials in minutes. " +
                        "Fresh products, lightning fast delivery, and great prices!\n\n" +
                        "© 2024 Zepto Inc. All rights reserved.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void logout() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Clear cart and any session data if needed
                    CartManager.getInstance().clearCart();

                    android.widget.Toast.makeText(this, "Logged out successfully", android.widget.Toast.LENGTH_SHORT).show();

                    // Navigate to login screen
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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
                // Already on Profile page
                return true;
            }
            return false;
        });

        // Set current item as selected
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh address when returning from AddressActivity
        loadUserData();
    }
}