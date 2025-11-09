package com.app.zepto;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView productName, productPrice, productDescription;
    private Button btnAddToCart;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        initializeViews();
        setupProductData();
        setupClickListeners();
        setupBottomNavigation();
    }

    private void initializeViews() {
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);
    }

    private void setupProductData() {
        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            productImage.setImageResource(product.getImageResId());
            productName.setText(product.getName());
            productPrice.setText(product.getPrice());
            productDescription.setText("Fresh and high quality " + product.getName() + ". Perfect for your daily needs. This product is carefully selected for its quality and freshness.");
        }
    }

    private void setupClickListeners() {
        btnAddToCart.setOnClickListener(v -> addToCart());
    }

    private void addToCart() {
        if (product != null) {
            CartManager.getInstance().addToCart(product);
            Toast.makeText(this, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
        }
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
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}