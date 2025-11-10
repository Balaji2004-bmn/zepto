package com.app.zepto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class OrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Order> orderList;
    private TextView tvEmptyOrders;
    private static final String TAG = "OrdersActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        initializeViews();
        loadOrdersFromStorage();
        setupRecyclerView();
        setupBottomNavigation();

        if (getIntent().getBooleanExtra("new_order", false)) {
            String orderId = getIntent().getStringExtra("order_id");
            Toast.makeText(this, "Order placed successfully! ID: " + orderId, Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.ordersRecyclerView);
        tvEmptyOrders = findViewById(R.id.tvEmptyOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadOrdersFromStorage() {
        try {
            orderList = new ArrayList<>();

            SharedPreferences prefs = getSharedPreferences("user_orders", MODE_PRIVATE);
            String ordersJson = prefs.getString("orders", "[]");

            Log.d(TAG, "Loading orders from JSON: " + ordersJson);

            Gson gson = new Gson();
            Type type = new TypeToken<List<Order>>() {}.getType();
            List<Order> loadedOrders = gson.fromJson(ordersJson, type);

            if (loadedOrders != null && !loadedOrders.isEmpty()) {
                orderList.addAll(loadedOrders);
                Log.d(TAG, "Successfully loaded " + orderList.size() + " orders");

                for (Order order : orderList) {
                    Log.d(TAG, "Order: " + order.getOrderId() + " - " + order.getTotalAmount() + " - " + order.getStatus());
                }
            } else {
                Log.d(TAG, "No orders found in storage");
                addSampleOrdersForTesting();
            }

            updateEmptyState();

        } catch (Exception e) {
            Log.e(TAG, "Error loading orders: " + e.getMessage(), e);
            Toast.makeText(this, "Error loading orders", Toast.LENGTH_SHORT).show();
            addSampleOrdersForTesting();
        }
    }

    private void addSampleOrdersForTesting() {
        Log.d(TAG, "Adding sample orders for testing");

        Order order1 = new Order();
        order1.setOrderId("ORD001");
        order1.setOrderDate("15 Jan 2024");
        order1.setTotalAmount(1250);
        order1.setStatus("Delivered");
        order1.setPaymentMethod("Cash on Delivery");

        List<String> items1 = new ArrayList<>();
        items1.add("Apples - 2kg");
        items1.add("Milk - 1L");
        order1.setItems(items1);

        Order order2 = new Order();
        order2.setOrderId("ORD002");
        order2.setOrderDate("14 Jan 2024");
        order2.setTotalAmount(850);
        order2.setStatus("Shipped");
        order2.setPaymentMethod("Card Payment");

        List<String> items2 = new ArrayList<>();
        items2.add("Bread - 2 packets");
        order2.setItems(items2);

        orderList.add(order1);
        orderList.add(order2);
    }

    private void updateEmptyState() {
        if (orderList == null || orderList.isEmpty()) {
            tvEmptyOrders.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            tvEmptyOrders.setText("No orders found. Start shopping!");
        } else {
            tvEmptyOrders.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView() {
        if (orderList == null) {
            orderList = new ArrayList<>();
        }
        ordersAdapter = new OrdersAdapter(this, orderList);
        recyclerView.setAdapter(ordersAdapter);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (bottomNavigationView != null) {
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
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(this, ProfileActivity.class));
                    finish();
                    return true;
                }
                return false;
            });

            bottomNavigationView.setSelectedItemId(R.id.nav_orders);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrdersFromStorage();
        if (ordersAdapter != null) {
            ordersAdapter.notifyDataSetChanged();
        }
    }
}
