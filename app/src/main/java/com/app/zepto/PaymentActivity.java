package com.app.zepto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {
    private double totalAmount;
    private TextView tvTotalAmount;
    private static final String TAG = "PaymentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvTotalAmount = findViewById(R.id.total_amount_textview);

        totalAmount = getIntent().getDoubleExtra("TOTAL_AMOUNT", 0);
        tvTotalAmount.setText("To Pay : ₹" + String.format("%.2f", totalAmount));

        Log.d(TAG, "Payment activity started with amount: " + totalAmount);

        // Set up click listeners
        findViewById(R.id.cashCard).setOnClickListener(v -> payWithCash(v));
        findViewById(R.id.cardCard).setOnClickListener(v -> payWithCard(v));

        Button btnAddUpi = findViewById(R.id.btn_add_upi);
        btnAddUpi.setOnClickListener(v -> {
            Toast.makeText(this, "Add new UPI ID feature", Toast.LENGTH_SHORT).show();
        });
    }

    public void payWithGpay(View view) {
        Toast.makeText(this, "Opening Google Pay...", Toast.LENGTH_SHORT).show();
        startUPIPayment("your_upi_id@okicici", "Google Pay");
    }

    public void payWithPhonepe(View view) {
        Toast.makeText(this, "Opening PhonePe...", Toast.LENGTH_SHORT).show();
        startUPIPayment("your_upi_id@ybl", "PhonePe");
    }

    public void payWithPaytm(View view) {
        Toast.makeText(this, "Opening Paytm...", Toast.LENGTH_SHORT).show();
        startUPIPayment("your_upi@paytm", "Paytm");
    }

    public void payWithCard(View view) {
        Toast.makeText(this, "Card payment selected", Toast.LENGTH_SHORT).show();
        // For demo, treat card payment as successful
        processPaymentSuccess("Card Payment");
    }

    public void payWithCash(View view) {
        Toast.makeText(this, "Cash on Delivery Selected! Order placed successfully.", Toast.LENGTH_LONG).show();
        processPaymentSuccess("Cash on Delivery");
    }

    private void processPaymentSuccess(String paymentMethod) {
        try {
            Log.d(TAG, "Processing payment success with method: " + paymentMethod);

            // Generate order
            Order newOrder = createOrder(paymentMethod);
            Log.d(TAG, "Created order: " + newOrder.getOrderId());

            // Save order to storage
            boolean saved = saveOrder(newOrder);
            Log.d(TAG, "Order saved: " + saved);

            if (saved) {
                // Show notification
                NotificationHelper.notifyOrderPlaced(this, newOrder.getOrderId(), totalAmount);

                // Clear cart
                CartManager.getInstance().clearCart();
                Log.d(TAG, "Cart cleared");

                // Navigate to orders with success
                Intent intent = new Intent(this, OrdersActivity.class);
                intent.putExtra("new_order", true);
                intent.putExtra("order_id", newOrder.getOrderId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to save order. Please try again.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in processPaymentSuccess: " + e.getMessage(), e);
            Toast.makeText(this, "Error processing payment: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Order createOrder(String paymentMethod) {
        Order order = new Order();
        order.setOrderId("ORD" + System.currentTimeMillis());
        order.setOrderDate(new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(new Date()));
        order.setTotalAmount(totalAmount);
        order.setStatus("Placed");
        order.setPaymentMethod(paymentMethod);

        // Add cart items to order
        List<String> orderItems = new ArrayList<>();
        List<CartItem> cartItems = CartManager.getInstance().getCartItems();

        Log.d(TAG, "Cart items count: " + cartItems.size());

        for (CartItem cartItem : cartItems) {
            String item = cartItem.getQuantity() + " x " + cartItem.getProduct().getName() + " - ₹" + cartItem.getProduct().getPrice();
            orderItems.add(item);
            Log.d(TAG, "Added to order: " + item);
        }
        order.setItems(orderItems);

        return order;
    }

    private boolean saveOrder(Order order) {
        try {
            android.content.SharedPreferences prefs = getSharedPreferences("user_orders", MODE_PRIVATE);
            String ordersJson = prefs.getString("orders", "[]");

            Log.d(TAG, "Existing orders JSON: " + ordersJson);

            Gson gson = new Gson();
            Type type = new TypeToken<List<Order>>() {}.getType();
            List<Order> orders = gson.fromJson(ordersJson, type);

            if (orders == null) {
                orders = new ArrayList<>();
                Log.d(TAG, "Orders list was null, created new list");
            }

            Log.d(TAG, "Current orders count before adding: " + orders.size());

            orders.add(0, order); // Add new order at beginning

            String newOrdersJson = gson.toJson(orders);
            Log.d(TAG, "New orders JSON: " + newOrdersJson);

            android.content.SharedPreferences.Editor editor = prefs.edit();
            editor.putString("orders", newOrdersJson);
            boolean saved = editor.commit(); // Use commit() for immediate result

            Log.d(TAG, "Order saved to SharedPreferences: " + saved);
            return saved;

        } catch (Exception e) {
            Log.e(TAG, "Error saving order: " + e.getMessage(), e);
            return false;
        }
    }

    private void startUPIPayment(String upiId, String appName) {
        try {
            Uri uri = Uri.parse("upi://pay?pa=" + upiId +
                    "&pn=Zepto+Store" +
                    "&mc=0000" +
                    "&tid=021254" +
                    "&tr=" + System.currentTimeMillis() +
                    "&tn=Zepto+Payment" +
                    "&am=" + totalAmount +
                    "&cu=INR");

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, appName + " app not found", Toast.LENGTH_SHORT).show();
        }
    }
}