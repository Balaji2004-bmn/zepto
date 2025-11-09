package com.app.zepto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private String orderDate;
    private double totalAmount;
    private String status;
    private String paymentMethod;
    private List<String> items;
    private List<TrackingEvent> trackingEvents;

    public Order() {
        this.trackingEvents = new ArrayList<>();
    }

    // Add this constructor
    public Order(String orderId, String orderDate, double totalAmount, String status, List<String> items, String paymentMethod) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
        this.paymentMethod = paymentMethod;
        this.trackingEvents = new ArrayList<>();
        initializeTracking();
    }

    private void initializeTracking() {
        trackingEvents.clear();

        trackingEvents.add(new TrackingEvent("Order Placed", "Your order has been confirmed", true));

        if ("Shipped".equals(status) || "Delivered".equals(status)) {
            trackingEvents.add(new TrackingEvent("Order Confirmed", "Restaurant has confirmed your order", true));
        }

        if ("Delivered".equals(status)) {
            trackingEvents.add(new TrackingEvent("Out for Delivery", "Your order is out for delivery", true));
            trackingEvents.add(new TrackingEvent("Delivered", "Your order has been delivered", true));
        }
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; initializeTracking(); }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; initializeTracking(); }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }

    public List<TrackingEvent> getTrackingEvents() { return trackingEvents; }
    public void setTrackingEvents(List<TrackingEvent> trackingEvents) { this.trackingEvents = trackingEvents; }
}