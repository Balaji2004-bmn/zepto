package com.app.zepto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ProductPage extends AppCompatActivity {

    private EditText searchBar;
    private ImageView profileIcon;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Product> filteredList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        // Initialize views - using correct IDs from your XML
        recyclerView = findViewById(R.id.recyclerview);
        searchBar = findViewById(R.id.search_bar);
        profileIcon = findViewById(R.id.profile);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Changed to 2 columns for better fit

        // Initialize product list
        productList = new ArrayList<>();
        productList.add(new Product("Fortune Sunflower", R.drawable.fortune, "₹156", "MRP ₹190", "1 l"));
        productList.add(new Product("Amul taaza Milk(Pouch)", R.drawable.amul_milk, "₹28", "MRP ₹30", "500ml"));
        productList.add(new Product("Onion", R.drawable.onion, "₹83", "MRP ₹111", "1 kg"));
        productList.add(new Product("Coconut", R.drawable.coconut, "₹89", "MRP ₹119", "1 "));
        productList.add(new Product("Amul Butter", R.drawable.amul_butter, "₹60", "MRP ₹90", "50 gm"));
        productList.add(new Product("Coriander", R.drawable.corianderc, "₹18", "MRP ₹24", "1 "));

        filteredList = new ArrayList<>(productList);
        productAdapter = new ProductAdapter(this, filteredList);
        recyclerView.setAdapter(productAdapter);

        // Search functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Profile icon click
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductPage.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Already on home page
                    return true;
                } else if (itemId == R.id.nav_categories) {
                    startActivity(new Intent(ProductPage.this, CategoryActivity.class));
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(ProductPage.this, CartActivity.class));
                    return true;
                } else if (itemId == R.id.nav_orders) {
                    startActivity(new Intent(ProductPage.this, OrdersActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Handle profile navigation
                    // startActivity(new Intent(ProductPage.this, ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

// Set home as selected since we're on home page
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Display phone number if passed from MainActivity
        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phone_number");
        if (phoneNumber != null) {
            Toast.makeText(this, "Welcome! Phone: " + phoneNumber, Toast.LENGTH_SHORT).show();
        }
    }

    private void filterProducts(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        }
        productAdapter.notifyDataSetChanged();
    }
}