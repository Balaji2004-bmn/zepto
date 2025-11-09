package com.app.zepto;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity {
    private EditText etFullName, etMobile, etPincode, etAddress, etLandmark, etCity, etState;
    private Button btnSaveAddress, btnDeleteAddress;
    private RadioGroup rgAddressType;
    private RecyclerView rvSavedAddresses;
    private LinearLayout llAddressForm, llSavedAddresses;
    private TextView tvNoAddresses;

    private List<Address> addressList;
    private AddressAdapter addressAdapter;
    private Address currentAddress;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        initializeViews();
        setupClickListeners();
        setupRecyclerView();
        loadSavedAddresses();
        setupBottomNavigation();

        // Check if we're editing an existing address from intent
        checkEditIntent();
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
        btnDeleteAddress = findViewById(R.id.btnDeleteAddress);

        rgAddressType = findViewById(R.id.rgAddressType);
        rvSavedAddresses = findViewById(R.id.rvSavedAddresses);
        llAddressForm = findViewById(R.id.llAddressForm);
        llSavedAddresses = findViewById(R.id.llSavedAddresses);
        tvNoAddresses = findViewById(R.id.tvNoAddresses);

        addressList = new ArrayList<>();
    }

    private void setupClickListeners() {
        btnSaveAddress.setOnClickListener(v -> saveAddress());
        btnDeleteAddress.setOnClickListener(v -> deleteAddress());

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());

        // REMOVED: Reference to non-existent btnAddNewAddress
        // If you want to add a way to show the form, you can add a "Add New Address" button in the layout
    }

    private void setupRecyclerView() {
        addressAdapter = new AddressAdapter(this, addressList, new AddressAdapter.AddressClickListener() {
            @Override
            public void onAddressClick(Address address) {
                // Set as default address or edit
                editAddress(address);
            }

            @Override
            public void onEditClick(Address address) {
                editAddress(address);
            }

            @Override
            public void onDeleteClick(Address address) {
                deleteSpecificAddress(address);
            }

            @Override
            public void onSetDefaultClick(Address address) {
                setDefaultAddress(address);
            }
        });

        rvSavedAddresses.setLayoutManager(new LinearLayoutManager(this));
        rvSavedAddresses.setAdapter(addressAdapter);
    }

    private void checkEditIntent() {
        if (getIntent().hasExtra("edit_address")) {
            Address addressToEdit = (Address) getIntent().getSerializableExtra("edit_address");
            if (addressToEdit != null) {
                editAddress(addressToEdit);
            }
        }
    }

    private void loadSavedAddresses() {
        android.content.SharedPreferences prefs = getSharedPreferences("user_addresses", MODE_PRIVATE);
        String addressesJson = prefs.getString("address_list", "[]");

        Gson gson = new Gson();
        Type type = new TypeToken<List<Address>>() {}.getType();
        addressList = gson.fromJson(addressesJson, type);

        if (addressList == null) {
            addressList = new ArrayList<>();
        }

        updateAddressUI();
    }

    private void updateAddressUI() {
        if (addressList.isEmpty()) {
            tvNoAddresses.setVisibility(View.VISIBLE);
            rvSavedAddresses.setVisibility(View.GONE);
            showAddressForm(); // Show form if no addresses
        } else {
            tvNoAddresses.setVisibility(View.GONE);
            rvSavedAddresses.setVisibility(View.VISIBLE);
            addressAdapter.updateAddressList(addressList);
            showSavedAddresses(); // Show list if addresses exist
        }
    }

    private void showAddressForm() {
        llAddressForm.setVisibility(View.VISIBLE);
        llSavedAddresses.setVisibility(View.GONE);
        resetForm();
    }

    private void showSavedAddresses() {
        llAddressForm.setVisibility(View.GONE);
        llSavedAddresses.setVisibility(View.VISIBLE);
    }

    private void editAddress(Address address) {
        currentAddress = address;
        isEditing = true;

        // Fill form with address data
        etFullName.setText(address.getFullName());
        etMobile.setText(address.getMobile());
        etPincode.setText(address.getPincode());
        etAddress.setText(address.getAddress());
        etLandmark.setText(address.getLandmark());
        etCity.setText(address.getCity());
        etState.setText(address.getState());

        // Set address type
        if (address.getAddressType().equals("Home")) {
            rgAddressType.check(R.id.rbHome);
        } else if (address.getAddressType().equals("Work")) {
            rgAddressType.check(R.id.rbWork);
        } else {
            rgAddressType.check(R.id.rbOther);
        }

        // Update UI for editing
        btnSaveAddress.setText("Update Address");
        btnDeleteAddress.setVisibility(View.VISIBLE);

        showAddressForm();
    }

    private void saveAddress() {
        String fullName = etFullName.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String pincode = etPincode.getText().toString().trim();
        String addressText = etAddress.getText().toString().trim();
        String landmark = etLandmark.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String state = etState.getText().toString().trim();

        // Get address type
        String addressType = "Home";
        int selectedId = rgAddressType.getCheckedRadioButtonId();
        if (selectedId == R.id.rbWork) {
            addressType = "Work";
        } else if (selectedId == R.id.rbOther) {
            addressType = "Other";
        }

        if (validateInput(fullName, mobile, pincode, addressText, city, state)) {
            Address address;

            if (isEditing) {
                // Update existing address
                address = currentAddress;
                address.setFullName(fullName);
                address.setMobile(mobile);
                address.setPincode(pincode);
                address.setAddress(addressText);
                address.setLandmark(landmark);
                address.setCity(city);
                address.setState(state);
                address.setAddressType(addressType);
            } else {
                // Create new address
                address = new Address(
                        fullName, mobile, pincode, addressText,
                        landmark, city, state, addressType
                );
                addressList.add(address);
            }

            saveAddressesToStorage();

            String message = isEditing ? "Address updated successfully!" : "Address saved successfully!";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            // Reset and show list
            resetForm();
            updateAddressUI();
        }
    }

    private void deleteAddress() {
        if (currentAddress != null) {
            deleteSpecificAddress(currentAddress);
        }
    }

    private void deleteSpecificAddress(Address address) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Delete Address")
                .setMessage("Are you sure you want to delete this address?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    addressList.remove(address);
                    saveAddressesToStorage();
                    updateAddressUI();
                    Toast.makeText(this, "Address deleted successfully!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setDefaultAddress(Address address) {
        for (Address addr : addressList) {
            addr.setDefault(addr.getId().equals(address.getId()));
        }
        saveAddressesToStorage();
        addressAdapter.updateAddressList(addressList);
        Toast.makeText(this, "Default address set to " + address.getAddressType(), Toast.LENGTH_SHORT).show();
    }

    private void resetForm() {
        etFullName.setText("");
        etMobile.setText("");
        etPincode.setText("");
        etAddress.setText("");
        etLandmark.setText("");
        etCity.setText("");
        etState.setText("");
        rgAddressType.check(R.id.rbHome);

        btnSaveAddress.setText("Save Address");
        btnDeleteAddress.setVisibility(View.GONE);
        isEditing = false;
        currentAddress = null;
    }

    private void saveAddressesToStorage() {
        android.content.SharedPreferences prefs = getSharedPreferences("user_addresses", MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String addressesJson = gson.toJson(addressList);
        editor.putString("address_list", addressesJson);
        editor.apply();
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
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });

        // Set current item as selected
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
    }

    @Override
    public void onBackPressed() {
        if (llAddressForm.getVisibility() == View.VISIBLE && !addressList.isEmpty()) {
            showSavedAddresses();
        } else {
            super.onBackPressed();
        }
    }
}