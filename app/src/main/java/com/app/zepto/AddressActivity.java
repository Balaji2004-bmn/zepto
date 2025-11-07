package com.app.zepto;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddressActivity extends AppCompatActivity {
    private EditText etFullName, etMobile, etPincode, etAddress, etLandmark, etCity, etState;
    private Button btnSaveAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        initializeViews();
        setupClickListeners();
        setupBottomNavigation();
    }

    private void initializeViews() {
        etFullName = findViewById(R.id.etFullName);
        etMobile = findViewById(R.id.etMobile);
        etPincode = findViewById(R.id.etPincode);
        etAddress = findViewById(R.id.etAddress);
        etLandmark = findViewById(R.id.etLandmark);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        btnSaveAddress = findViewById(R.id.btnSaveAddress);
    }

    private void setupClickListeners() {
        btnSaveAddress.setOnClickListener(v -> saveAddress());
    }

    private void saveAddress() {
        String fullName = etFullName.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String pincode = etPincode.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String landmark = etLandmark.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String state = etState.getText().toString().trim();

        if (validateInput(fullName, mobile, pincode, address, city, state)) {
            // Save address using SharedPreferences
            android.content.SharedPreferences prefs = getSharedPreferences("user_address", MODE_PRIVATE);
            android.content.SharedPreferences.Editor editor = prefs.edit();
            editor.putString("full_name", fullName);
            editor.putString("mobile", mobile);
            editor.putString("pincode", pincode);
            editor.putString("address", address);
            editor.putString("landmark", landmark);
            editor.putString("city", city);
            editor.putString("state", state);
            editor.apply();

            Toast.makeText(this, "Address saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean validateInput(String fullName, String mobile, String pincode,
                                  String address, String city, String state) {
        if (fullName.isEmpty()) {
            showError(etFullName, "Please enter full name");
            return false;
        }
        if (mobile.isEmpty() || mobile.length() != 10) {
            showError(etMobile, "Please enter valid 10-digit mobile number");
            return false;
        }
        if (pincode.isEmpty() || pincode.length() != 6) {
            showError(etPincode, "Please enter valid 6-digit pincode");
            return false;
        }
        if (address.isEmpty()) {
            showError(etAddress, "Please enter address");
            return false;
        }
        if (city.isEmpty()) {
            showError(etCity, "Please enter city");
            return false;
        }
        if (state.isEmpty()) {
            showError(etState, "Please enter state");
            return false;
        }
        return true;
    }

    private void showError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, ProductPage.class));
                return true;
            } else if (itemId == R.id.nav_categories) {
                startActivity(new Intent(this, CategoryActivity.class));
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_orders) {
                startActivity(new Intent(this, OrdersActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Handle profile navigation
                // startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
}