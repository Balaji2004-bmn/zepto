package com.app.zepto;

import java.util.List;

public class Order {
    private String orderId;
    private String orderDate;
    private int totalAmount;
    private String status;
    private List<String> items;

    public Order() {}

    // Getters and setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }
}
