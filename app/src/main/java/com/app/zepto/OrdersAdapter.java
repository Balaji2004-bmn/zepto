package com.app.zepto;

import android.content.Context;
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
        holder.orderDate.setText(order.getOrderDate());
        holder.orderTotal.setText("₹" + order.getTotalAmount());
        holder.orderStatus.setText(order.getStatus());

        // Display items
        StringBuilder itemsText = new StringBuilder();
        if (order.getItems() != null) {
            for (String item : order.getItems()) {
                itemsText.append("• ").append(item).append("\n");
            }
        }
        holder.orderItems.setText(itemsText.toString().trim());

        // Set status color
        setStatusColor(holder.orderStatus, order.getStatus());
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
            default:
                colorResId = R.color.gray;
        }
        // Use ContextCompat for better compatibility
        statusView.setBackgroundColor(ContextCompat.getColor(context, colorResId));
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