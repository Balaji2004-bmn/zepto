package com.app.zepto.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "cart_items")
public class CartEntity {
    @PrimaryKey
    @NonNull
    private String productId;
    private String productName;
    private String price;
    private int quantity;
    private String imageUrl;

    // No-arg constructor (required by Room)
    public CartEntity() {
        this.productId = "";
    }

    // Mark this constructor with @Ignore to fix the warning
    @Ignore
    public CartEntity(@NonNull String productId, String productName, String price, int quantity, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    @NonNull
    public String getProductId() { return productId; }
    public void setProductId(@NonNull String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
