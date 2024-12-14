package com.example.ecommerceapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.textViewName.setText(product.getTitle());
        holder.textViewPrice.setText("Price: $" + String.format("%.2f", product.getPrice()));

        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.product_1)
                .error(R.drawable.starting_img)
                .into(holder.imageViewProduct);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, details_activity.class);
            intent.putExtra("productId", product.getId());
            intent.putExtra("productTitle", product.getTitle());
            intent.putExtra("productPrice", product.getPrice());
            intent.putExtra("productImage", product.getImage());
            intent.putExtra("productDescription", product.getDescription());
            intent.putExtra("itemDetails", product.getItemDetails());
            context.startActivity(intent);
        });

        CartItem cartItem = CartManager.getInstance().getCart().get(product.getId());

        if (cartItem != null) {
            holder.buttonAddToCart.setVisibility(View.GONE);
            holder.layoutQuantity.setVisibility(View.VISIBLE);
            holder.textQuantity.setText(String.valueOf(cartItem.getQuantity()));
        } else {

            holder.buttonAddToCart.setVisibility(View.VISIBLE);
            holder.layoutQuantity.setVisibility(View.GONE);
        }

        holder.buttonAddToCart.setOnClickListener(view -> {
            CartManager.getInstance().addToCart(
                    new CartItem(product.getId(), product.getTitle(), product.getImage(), product.getPrice(), 1, product.getDescription(), product.getItemDetails())
            );
            notifyItemChanged(position);
        });

        holder.buttonIncrease.setOnClickListener(view -> {
            if (cartItem != null) {
                int newQuantity = cartItem.getQuantity() + 1;
                if (newQuantity <= 10) {
                    CartManager.getInstance().updateQuantity(product.getId(), newQuantity);
                    notifyItemChanged(position); // Refresh the UI
                } else {
                    Toast.makeText(context, "Maximum limit reached", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.buttonDecrease.setOnClickListener(view -> {
            if (cartItem != null) {
                if (cartItem.getQuantity() > 1) {

                    int newQuantity = cartItem.getQuantity() - 1;
                    CartManager.getInstance().updateQuantity(product.getId(), newQuantity);
                } else {

                    CartManager.getInstance().removeFromCart(product.getId());
                }
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice, textQuantity;
        ImageView imageViewProduct;
        Button buttonAddToCart, buttonIncrease, buttonDecrease;
        LinearLayout layoutQuantity;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            buttonAddToCart = itemView.findViewById(R.id.buttonAddToCart);
            buttonIncrease = itemView.findViewById(R.id.buttonIncrease);
            buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
            layoutQuantity = itemView.findViewById(R.id.layoutQuantity);
            textQuantity = itemView.findViewById(R.id.textQuantity);
        }
    }
}
