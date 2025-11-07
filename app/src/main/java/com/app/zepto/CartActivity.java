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
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvTotalPrice, tvEmptyCart, tvSavingsBanner;
    private Button btnCheckout;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems; // Changed from Product to CartItem

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initializeViews();
        setupCartItems();
        setupCheckoutButton();
        checkCartEmpty();
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

            // Handle price format (remove ₹ symbol and convert to double)
            String priceString = product.getPrice().replace("₹", "").trim();
            try {
                double price = Double.parseDouble(priceString);
                total += price * quantity; // Multiply by quantity
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