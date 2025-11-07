package com.app.zepto;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up back button
        findViewById(R.id.settingsBackButton).setOnClickListener(v -> onBackPressed());

        // Set up click listeners using IDs
        setupClickListeners();

        // Set up food preferences
        setupFoodPreferences();
    }

    private void setupClickListeners() {
        // Orders items
        findViewById(R.id.yourOrdersLayout).setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, OrdersActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.ordersLayout).setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, OrdersActivity.class);
            startActivity(intent);
        });

        // Help Support
        findViewById(R.id.helpSupportLayout).setOnClickListener(v -> {
            Toast.makeText(SettingsActivity.this, "Help & Support", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.supportLayout).setOnClickListener(v -> {
            Toast.makeText(SettingsActivity.this, "Customer Support & FAQ", Toast.LENGTH_SHORT).show();
        });

        // Address
        findViewById(R.id.addressLayout).setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AddressActivity.class);
            startActivity(intent);
        });

        // Zepto Cash
        findViewById(R.id.zeptoCashLayout).setOnClickListener(v -> {
            Toast.makeText(SettingsActivity.this, "Zepto Cash", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupFoodPreferences() {
        RadioGroup radioGroup = findViewById(R.id.foodPreferencesGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String preference = "";
            if (checkedId == R.id.radioVegetarian) {
                preference = "Vegetarian";
            } else if (checkedId == R.id.radioNonVegetarian) {
                preference = "Non-Vegetarian";
            } else if (checkedId == R.id.radioEggitarian) {
                preference = "Eggitarian";
            }
            Toast.makeText(SettingsActivity.this, "Food preference: " + preference, Toast.LENGTH_SHORT).show();
        });
    }

    // Method for Zepto Cash image click (XML onClick)
    public void zeptoCash(View view) {
        Toast.makeText(this, "Zepto Cash", Toast.LENGTH_SHORT).show();
    }

    // ⭐⭐⭐ ADD MISSING PAYMENT METHODS FOR XML onClick ATTRIBUTES ⭐⭐⭐

    public void payWithPhonepe(View view) {
        Toast.makeText(this, "PhonePe Payment - Redirecting...", Toast.LENGTH_SHORT).show();
        // You can add navigation to payment page if needed
        // Intent intent = new Intent(this, PaymentActivity.class);
        // startActivity(intent);
    }

    public void payWithGpay(View view) {
        Toast.makeText(this, "GPay Payment - Redirecting...", Toast.LENGTH_SHORT).show();
        // You can add navigation to payment page if needed
        // Intent intent = new Intent(this, PaymentActivity.class);
        // startActivity(intent);
    }
}