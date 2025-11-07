package com.app.zepto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {
    private int totalAmount;
    private TextView tvTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvTotalAmount = findViewById(R.id.total_amount_textview);

        totalAmount = getIntent().getIntExtra("TOTAL_AMOUNT", 0);
        tvTotalAmount.setText("To Pay : ₹" + totalAmount);

        // Set up click listener for Cash on Delivery card
        findViewById(R.id.cashCard).setOnClickListener(v -> payWithCash(v));

        // Set up click listener for Credit/Debit card
        findViewById(R.id.cardCard).setOnClickListener(v -> payWithCard(v));

        // Set up click listener for Add UPI button
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

    // ⭐⭐⭐ ADDED: Missing payWithPaytm method ⭐⭐⭐
    public void payWithPaytm(View view) {
        Toast.makeText(this, "Opening Paytm...", Toast.LENGTH_SHORT).show();
        startUPIPayment("your_upi@paytm", "Paytm");
    }

    // ⭐⭐⭐ ADDED: Method for card payment ⭐⭐⭐
    public void payWithCard(View view) {
        Toast.makeText(this, "Card payment selected", Toast.LENGTH_SHORT).show();
        // Add card payment logic here
    }

    // ⭐⭐⭐ ADDED: Method for cash payment ⭐⭐⭐
    public void payWithCash(View view) {
        Toast.makeText(this, "Cash on Delivery Selected! Order placed successfully.", Toast.LENGTH_LONG).show();
        // Clear cart and go to success page
        CartManager.getInstance().clearCart();
        Intent intent = new Intent(this, OrdersActivity.class);
        startActivity(intent);
        finish();
    }

    private void startUPIPayment(String upiId, String appName) {
        try {
            Uri uri = Uri.parse("upi://pay?pa=" + upiId +
                    "&pn=Zepto+Store" +
                    "&mc=0000" +
                    "&tid=021254" +
                    "&tr=123456789" +
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