package com.app.zepto;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Order> orderList;
    private TextView tvEmptyOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        initializeViews();
        setupOrderData();
        setupRecyclerView();
        setupBottomNavigation();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.ordersRecyclerView);
        tvEmptyOrders = findViewById(R.id.tvEmptyOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupOrderData() {
        orderList = new ArrayList<>();

        // Sample order data
        Order order1 = new Order();
        order1.setOrderId("ORD001");
        order1.setOrderDate("15 Jan 2024");
        order1.setTotalAmount(1250);
        order1.setStatus("Delivered");

        List<String> items1 = new ArrayList<>();
        items1.add("Apples - 2kg");
        items1.add("Milk - 1L");
        order1.setItems(items1);

        Order order2 = new Order();
        order2.setOrderId("ORD002");
        order2.setOrderDate("14 Jan 2024");
        order2.setTotalAmount(850);
        order2.setStatus("Shipped");

        List<String> items2 = new ArrayList<>();
        items2.add("Bread - 2 packets");
        order2.setItems(items2);

        orderList.add(order1);
        orderList.add(order2);

        updateEmptyState();
    }

    private void updateEmptyState() {
        if (orderList.isEmpty()) {
            tvEmptyOrders.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyOrders.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView() {
        ordersAdapter = new OrdersAdapter(this, orderList);
        recyclerView.setAdapter(ordersAdapter);
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
                // Already on Orders page
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Handle profile navigation
                // startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });

        // Set Orders as selected
        bottomNavigationView.setSelectedItemId(R.id.nav_orders);
    }
    }
