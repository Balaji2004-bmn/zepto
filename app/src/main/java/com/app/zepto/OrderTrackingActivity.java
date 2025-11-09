package com.app.zepto;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.List;

public class OrderTrackingActivity extends AppCompatActivity {
    private LinearLayout trackingContainer;
    private TextView orderIdText, orderStatusText, orderDateText, orderTotalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        initializeViews();

        Order order = (Order) getIntent().getSerializableExtra("order");
        if (order != null) {
            displayOrderTracking(order);
        }

        setupClickListeners();
    }

    private void initializeViews() {
        trackingContainer = findViewById(R.id.trackingContainer);
        orderIdText = findViewById(R.id.orderIdText);
        orderStatusText = findViewById(R.id.orderStatusText);
        orderDateText = findViewById(R.id.orderDateText);
        orderTotalText = findViewById(R.id.orderTotalText);
    }

    private void displayOrderTracking(Order order) {
        orderIdText.setText("Order #" + order.getOrderId());
        orderStatusText.setText("Status: " + order.getStatus());
        orderDateText.setText("Order Date: " + order.getOrderDate());
        orderTotalText.setText("Total Amount: ₹" + order.getTotalAmount());

        // Display tracking steps
        List<TrackingEvent> events = order.getTrackingEvents();
        trackingContainer.removeAllViews();

        for (int i = 0; i < events.size(); i++) {
            TrackingEvent event = events.get(i);
            addTrackingStep(event, i, events.size());
        }
    }

    private void addTrackingStep(TrackingEvent event, int position, int totalSteps) {
        View stepView = getLayoutInflater().inflate(R.layout.item_tracking_step, trackingContainer, false);

        TextView stepName = stepView.findViewById(R.id.stepName);
        TextView stepDesc = stepView.findViewById(R.id.stepDescription);
        View stepIndicator = stepView.findViewById(R.id.stepIndicator);
        View stepConnector = stepView.findViewById(R.id.stepConnector);

        stepName.setText(event.getEventName());
        stepDesc.setText(event.getDescription());

        // Set colors based on completion status
        if (event.isCompleted()) {
            stepIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            stepName.setTextColor(ContextCompat.getColor(this, R.color.green));
        } else {
            stepIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            stepName.setTextColor(ContextCompat.getColor(this, R.color.gray));
        }

        // Hide connector for last step
        if (position == totalSteps - 1) {
            stepConnector.setVisibility(View.GONE);
        }

        trackingContainer.addView(stepView);
    }

    private void setupClickListeners() {
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        findViewById(R.id.btnContactSupport).setOnClickListener(v -> {
            // Implement contact support functionality
            android.widget.Toast.makeText(this, "Contacting customer support...",
                    android.widget.Toast.LENGTH_SHORT).show();
        });
    }
}
