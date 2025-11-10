package com.app.zepto;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }


    public void addToCart(Product product) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getName().equals(product.getName())) {
                // Product already in cart, increase quantity
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
       
        cartItems.add(new CartItem(product, 1));
    }

    public void removeFromCart(Product product) {
        cartItems.removeIf(item -> item.getProduct().getName().equals(product.getName()));
    }

    public void updateQuantity(Product product, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getName().equals(product.getName())) {
                if (quantity <= 0) {
                    cartItems.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                break;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double getTotalAmount() {
        double total = 0;
        for (CartItem item : cartItems) {
            String priceStr = item.getProduct().getPrice().replace("₹", "").trim();
            try {
                double price = Double.parseDouble(priceStr);
                total += price * item.getQuantity();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public int getTotalItems() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getQuantity();
        }
        return total;
    }
}
