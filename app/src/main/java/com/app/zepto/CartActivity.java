package com.app.zepto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvTotalPrice, tvEmptyCart, tvSavingsBanner;
    private Button btnCheckout;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initializeViews();
        setupCartItems();
        setupCheckoutButton();
        checkCartEmpty();
        setupBottomNavigation(); // ADDED
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.cart_list);
        tvTotalPrice = findViewById(R.id.total_price);
        btnCheckout = findViewById(R.id.btn_checkout);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        tvSavingsBanner = findViewById(R.id.savingsbanner);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupCartItems() {
        cartItems = CartManager.getInstance().getCartItems();
        cartAdapter = new CartAdapter(this, cartItems, new CartAdapter.OnCartUpdateListener() {
            @Override
            public void onCartUpdated() {
                refreshCart();
            }
        });
        recyclerView.setAdapter(cartAdapter);
        calculateTotal();
    }

    private void setupCheckoutButton() {
        btnCheckout.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(CartActivity.this,
                        "Your cart is empty. Add products to continue.",
                        Toast.LENGTH_LONG).show();
            } else {
                double totalAmount = calculateTotal();
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                intent.putExtra("TOTAL_AMOUNT", totalAmount);
                startActivity(intent);
            }
        });
    }

    private double calculateTotal() {
        double total = 0;
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            String priceString = product.getPrice().replace("₹", "").trim();
            try {
                double price = Double.parseDouble(priceString);
                total += price * quantity;
            } catch (NumberFormatException e) {
                total += 0;
            }
        }
        tvTotalPrice.setText(String.format("Total : ₹%.2f", total));
        checkCartEmpty();
        return total;
    }

    private void checkCartEmpty() {
        if (tvEmptyCart == null) return;

        if (cartItems.isEmpty()) {
            tvEmptyCart.setVisibility(View.VISIBLE);
            tvEmptyCart.setText("Your cart is empty. Add products to continue.");
            recyclerView.setVisibility(View.GONE);
            if (tvSavingsBanner != null) {
                tvSavingsBanner.setVisibility(View.GONE);
            }
            btnCheckout.setEnabled(false);
            btnCheckout.setAlpha(0.5f);
            btnCheckout.setText("Cart is Empty");
        } else {
            tvEmptyCart.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (tvSavingsBanner != null) {
                tvSavingsBanner.setVisibility(View.VISIBLE);
            }
            btnCheckout.setEnabled(true);
            btnCheckout.setAlpha(1.0f);
            btnCheckout.setText("Proceed for Payment");
        }
    }

    // FIXED: Bottom Navigation Method with null check
    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Add null check to prevent crash
        if (bottomNavigationView == null) {
            return; // Exit if bottom navigation is not found
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
                // Already on Cart page
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
        bottomNavigationView.setSelectedItemId(R.id.nav_cart);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCart();
    }

    private void refreshCart() {
        cartItems = CartManager.getInstance().getCartItems();
        if (cartAdapter == null) {
            cartAdapter = new CartAdapter(this, cartItems, new CartAdapter.OnCartUpdateListener() {
                @Override
                public void onCartUpdated() {
                    refreshCart();
                }
            });
            recyclerView.setAdapter(cartAdapter);
        } else {
            cartAdapter.updateCartItems(cartItems);
        }
        calculateTotal();
        checkCartEmpty();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}