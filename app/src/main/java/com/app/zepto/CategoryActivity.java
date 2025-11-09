package com.app.zepto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private TextView searchBar;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private CategoryAdapter categoryAdapter,categoryAdapter2;
    private List<Category> categoryList;
    private List<Category> otherCategories;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView1 = findViewById(R.id.recyclerview1);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView1.setLayoutManager(new GridLayoutManager(this, 3));

        categoryList = new ArrayList<>();
        categoryList.add(new Category("Fruits & Vegetables", R.drawable.fruit));
        categoryList.add(new Category("Dairy, Bread & Eggs", R.drawable.bread));
        categoryList.add(new Category("Atta, Rice, Oil & Dals", R.drawable.ata));
        categoryList.add(new Category("Masala & Dry Fruits", R.drawable.dryfruit));
        categoryList.add(new Category("Packaged Food", R.drawable.maggie));

        otherCategories = new ArrayList<>();
        otherCategories.add(new Category("Tea, Coffee & More", R.drawable.tea));
        otherCategories.add(new Category("Frozen Food", R.drawable.frozen));
        otherCategories.add(new Category("Ice creams & More", R.drawable.icecream));
        otherCategories.add(new Category("Snacks", R.drawable.snacks));

        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryAdapter2 = new CategoryAdapter(this, otherCategories);
        recyclerView.setAdapter(categoryAdapter);
        recyclerView1.setAdapter(categoryAdapter2);

        setupBottomNavigation();
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
                // Already on categories page
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

        // Set categories as selected since we're on categories page
        bottomNavigationView.setSelectedItemId(R.id.nav_categories);
    }
}