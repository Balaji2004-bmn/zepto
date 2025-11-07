package com.app.zepto.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;
import java.util.List;

@Dao
public interface AppDao {
    // Product operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(ProductEntity product);

    @Query("SELECT * FROM products")
    List<ProductEntity> getAllProducts();

    @Query("SELECT * FROM products WHERE id = :productId")
    ProductEntity getProductById(String productId);

    // Cart operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCartItem(CartEntity cartItem);

    @Query("SELECT * FROM cart_items")
    List<CartEntity> getCartItems();

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    void removeCartItem(String productId);

    @Query("DELETE FROM cart_items")
    void clearCart();

    @Query("SELECT COUNT(*) FROM cart_items")
    int getCartItemCount();
}
