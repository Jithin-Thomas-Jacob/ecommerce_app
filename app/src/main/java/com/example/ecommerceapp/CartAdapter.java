package com.example.ecommerceapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private Runnable onCartUpdated;

    public CartAdapter(List<CartItem> cartItems, Runnable onCartUpdated){
        this.cartItems = cartItems;
        this.onCartUpdated = onCartUpdated;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cart_card, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        Log.d("CartAdapter", "Binding item: " + item.getTitle()); // Debugging

        holder.productTitle.setText(item.getTitle());
        holder.productPrice.setText("Price: $" + item.getPrice());
        holder.quantityText.setText(String.valueOf(item.getQuantity()));
        Glide.with(holder.productImage.getContext())
                .load(item.getImage())
                .into(holder.productImage);

        // Handle increase quantity
        holder.increaseQuantity.setOnClickListener(v -> {
            if(item.getQuantity() < 10) {
                item.setQuantity(item.getQuantity() + 1);
                CartManager.getInstance().updateQuantity(item.getId(), item.getQuantity());
                notifyItemChanged(position);
                onCartUpdated.run();
            } else {
                Toast.makeText(holder.itemView.getContext(), "Maximum limit reached", Toast.LENGTH_SHORT).show();
            }
        });

        holder.decreaseQuantity.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                CartManager.getInstance().updateQuantity(item.getId(), item.getQuantity());
                notifyItemChanged(position);
            } else {
                CartManager.getInstance().removeFromCart(item.getId());
                refreshCartData();
            }
            onCartUpdated.run();
        });

        holder.removeButton.setOnClickListener(v -> {
            CartManager.getInstance().removeFromCart(item.getId());
            refreshCartData();
            onCartUpdated.run();
        });
    }

    private void refreshCartData() {
        cartItems.clear();
        cartItems.addAll(CartManager.getInstance().getCart().values());
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productTitle, productPrice, quantityText;
        ImageView productImage;
        Button increaseQuantity, decreaseQuantity, removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantityText = itemView.findViewById(R.id.quantityText);
            productImage = itemView.findViewById(R.id.productImage);
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decreaseQuantity);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
