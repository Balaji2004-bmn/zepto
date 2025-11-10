package com.app.zepto.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "products")
public class ProductEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String price;
    private String imageUrl;
    private String category;

    public ProductEntity() {
        this.id = "";
    }

    @Ignore
    public ProductEntity(@NonNull String id, String name, String price, String imageUrl, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

   
    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
