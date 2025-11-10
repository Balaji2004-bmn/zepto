package com.app.zepto;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orderList;

    public OrdersAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.orderId.setText("Order #" + order.getOrderId());
        holder.orderDate.setText("Order Date: " + order.getOrderDate());
        holder.orderTotal.setText("Total: ₹" + order.getTotalAmount());
        holder.orderStatus.setText(order.getStatus());

        StringBuilder itemsText = new StringBuilder();
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            for (String item : order.getItems()) {
                itemsText.append("• ").append(item).append("\n");
            }
        } else {
            itemsText.append("No items in this order");
        }
        holder.orderItems.setText(itemsText.toString().trim());
        setStatusColor(holder.orderStatus, order.getStatus());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderTrackingActivity.class);
            intent.putExtra("orderId", order.getOrderId());
            intent.putExtra("orderDate", order.getOrderDate());
            intent.putExtra("totalAmount", order.getTotalAmount());
            intent.putExtra("status", order.getStatus());
            intent.putExtra("paymentMethod", order.getPaymentMethod());

            if (order.getItems() != null) {
                intent.putExtra("items", order.getItems().toArray(new String[0]));
            }
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            showOrderOptions(order, position);
            return true;
        });
    }

    private void setStatusColor(TextView statusView, String status) {
        int colorResId;
        switch (status.toLowerCase()) {
            case "delivered":
                colorResId = R.color.green;
                break;
            case "shipped":
                colorResId = R.color.blue;
                break;
            case "processing":
                colorResId = R.color.orange;
                break;
            case "placed":
                colorResId = R.color.purple_500;
                break;
            case "cancelled":
                colorResId = R.color.red;
                break;
            default:
                colorResId = R.color.gray;
        }

        statusView.setTextColor(ContextCompat.getColor(context, colorResId));

        try {
            statusView.setBackground(ContextCompat.getDrawable(context, R.drawable.status_background));
        } catch (Exception e) {
        }
    }

    private void showOrderOptions(Order order, int position) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("Order Options")
                .setItems(new String[]{"Track Order", "Reorder", "Cancel Order"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent intent = new Intent(context, OrderTrackingActivity.class);
                            intent.putExtra("orderId", order.getOrderId());
                            intent.putExtra("orderDate", order.getOrderDate());
                            intent.putExtra("totalAmount", order.getTotalAmount());
                            intent.putExtra("status", order.getStatus());
                            intent.putExtra("paymentMethod", order.getPaymentMethod());

                            if (order.getItems() != null) {
                                intent.putExtra("items", order.getItems().toArray(new String[0]));
                            }
                            context.startActivity(intent);
                            break;
                        case 1:
                            reorderItems(order);
                            break;
                        case 2: 
                            cancelOrder(order, position);
                            break;
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void reorderItems(Order order) {
        android.widget.Toast.makeText(context, "Adding items to cart from order #" + order.getOrderId(),
                android.widget.Toast.LENGTH_SHORT).show();

    }

    private void cancelOrder(Order order, int position) {
        if (!order.getStatus().equalsIgnoreCase("delivered")) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
            builder.setTitle("Cancel Order")
                    .setMessage("Are you sure you want to cancel order #" + order.getOrderId() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        order.setStatus("Cancelled");
                        notifyItemChanged(position);
                        saveUpdatedOrder(order);
                        android.widget.Toast.makeText(context, "Order cancelled successfully",
                                android.widget.Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            android.widget.Toast.makeText(context, "Cannot cancel delivered order",
                    android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUpdatedOrder(Order order) {
        android.content.SharedPreferences prefs = context.getSharedPreferences("user_orders", Context.MODE_PRIVATE);
        String ordersJson = prefs.getString("orders", "[]");

        try {
            org.json.JSONArray ordersArray = new org.json.JSONArray(ordersJson);
            for (int i = 0; i < ordersArray.length(); i++) {
                org.json.JSONObject orderJson = ordersArray.getJSONObject(i);
                if (orderJson.getString("orderId").equals(order.getOrderId())) {
                    orderJson.put("status", order.getStatus());
                    break;
                }
            }

            android.content.SharedPreferences.Editor editor = prefs.edit();
            editor.putString("orders", ordersArray.toString());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to update order list
    public void updateOrderList(List<Order> newOrderList) {
        this.orderList = newOrderList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate, orderTotal, orderStatus, orderItems;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderTotal = itemView.findViewById(R.id.orderTotal);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderItems = itemView.findViewById(R.id.orderItems);
        }
    }
}
