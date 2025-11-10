package com.app.zepto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartUpdateListener listener;

    public interface OnCartUpdateListener {
        void onCartUpdated();
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartUpdateListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        Product product = cartItem.getProduct();
        int quantity = cartItem.getQuantity();

        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productQuantity.setText("Qty: " + quantity);

       
        double itemTotal = calculateItemTotal(product.getPrice(), quantity);
        holder.itemTotal.setText("Item Total: ₹" + String.format("%.2f", itemTotal));

        holder.productImage.setImageResource(product.getImageResId());

        holder.quantityText.setText(String.valueOf(quantity));

       
        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = quantity + 1;
            CartManager.getInstance().updateQuantity(product, newQuantity);
            if (listener != null) {
                listener.onCartUpdated();
            }
        });

       
        holder.btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                int newQuantity = quantity - 1;
                CartManager.getInstance().updateQuantity(product, newQuantity);
            } else {
               
                CartManager.getInstance().removeFromCart(product);
            }
            if (listener != null) {
                listener.onCartUpdated();
            }
        });

     
        holder.btnRemove.setOnClickListener(v -> {
            CartManager.getInstance().removeFromCart(product);
            if (listener != null) {
                listener.onCartUpdated();
            }
        });
    }

    private double calculateItemTotal(String priceString, int quantity) {
        try {
            String cleanPrice = priceString.replace("₹", "").trim();
            double price = Double.parseDouble(cleanPrice);
            return price * quantity;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateCartItems(List<CartItem> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity, quantityText, itemTotal;
        ImageView productImage;
        Button btnIncrease, btnDecrease, btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.cart_product_name);
            productPrice = itemView.findViewById(R.id.cart_product_price);
            productQuantity = itemView.findViewById(R.id.cart_product_quantity);
            productImage = itemView.findViewById(R.id.cart_product_image);
            quantityText = itemView.findViewById(R.id.cart_quantity_text);
            itemTotal = itemView.findViewById(R.id.cart_item_total);
            btnIncrease = itemView.findViewById(R.id.cart_btn_increase);
            btnDecrease = itemView.findViewById(R.id.cart_btn_decrease);
            btnRemove = itemView.findViewById(R.id.cart_btn_remove);
        }
    }
}
