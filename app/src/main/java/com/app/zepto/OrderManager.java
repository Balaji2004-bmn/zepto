package com.app.zepto;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderManager {
    private static OrderManager instance;
    private Context context;
    private SharedPreferences prefs;

    private OrderManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences("user_orders", Context.MODE_PRIVATE);
    }

    public static OrderManager getInstance(Context context) {
        if (instance == null) {
            instance = new OrderManager(context);
        }
        return instance;
    }

    public void saveOrder(Order order) {
        try {
            String ordersJson = prefs.getString("orders", "[]");
            JSONArray ordersArray = new JSONArray(ordersJson);

            JSONObject orderJson = new JSONObject();
            orderJson.put("orderId", order.getOrderId());
            orderJson.put("orderDate", order.getOrderDate());
            orderJson.put("totalAmount", order.getTotalAmount());
            orderJson.put("status", order.getStatus());
            orderJson.put("paymentMethod", order.getPaymentMethod());

            JSONArray itemsArray = new JSONArray();
            if (order.getItems() != null) {
                for (String item : order.getItems()) {
                    itemsArray.put(item);
                }
            }
            orderJson.put("items", itemsArray);

            ordersArray.put(orderJson);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("orders", ordersArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            String ordersJson = prefs.getString("orders", "[]");
            JSONArray ordersArray = new JSONArray(ordersJson);

            for (int i = 0; i < ordersArray.length(); i++) {
                JSONObject orderJson = ordersArray.getJSONObject(i);

                String orderId = orderJson.getString("orderId");
                String orderDate = orderJson.getString("orderDate");
                double totalAmount = orderJson.getDouble("totalAmount");
                String status = orderJson.getString("status");
                String paymentMethod = orderJson.getString("paymentMethod");

                List<String> items = new ArrayList<>();
                JSONArray itemsArray = orderJson.getJSONArray("items");
                for (int j = 0; j < itemsArray.length(); j++) {
                    items.add(itemsArray.getString(j));
                }

                Order order = new Order(orderId, orderDate, totalAmount, status, items, paymentMethod);
                orders.add(order);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public String generateOrderId() {
        return "ORD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
