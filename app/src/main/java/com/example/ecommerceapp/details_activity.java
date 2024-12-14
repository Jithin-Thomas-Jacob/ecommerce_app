package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class details_activity extends AppCompatActivity {

    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView backButton = findViewById(R.id.backButton);
        String source = getIntent().getStringExtra("source");

        backButton.setOnClickListener(v -> {
            finish();
        });

        // Initialize the CartManager singleton
        cartManager = CartManager.getInstance();

        // Retrieve product details from the Intent
        String productId = getIntent().getStringExtra("productId");
        String productTitle = getIntent().getStringExtra("productTitle");
        double productPrice = getIntent().getDoubleExtra("productPrice", 0.00);
        String productDescription = getIntent().getStringExtra("productDescription");
        String productDetails = getIntent().getStringExtra("itemDetails");
        String productImage = getIntent().getStringExtra("productImage");

        // Find views
        ImageView productImageView = findViewById(R.id.product_image);
        TextView productTitleView = findViewById(R.id.product_title);
        TextView productPriceView = findViewById(R.id.product_price);
        TextView productDescriptionView = findViewById(R.id.product_description);
        TextView productDetailsView = findViewById(R.id.product_details);
        Button addToCartButton = findViewById(R.id.add_to_cart_button);
        LinearLayout quantityLayout = findViewById(R.id.quantity_layout);
        TextView quantityText = findViewById(R.id.quantity_text);
        Button decreaseButton = findViewById(R.id.decrease_button);
        Button increaseButton = findViewById(R.id.increase_button);

        // Populate UI with product details
        productTitleView.setText(productTitle != null ? productTitle : "No Title");
        productPriceView.setText("Price: $" + String.format("%.2f", productPrice));
        productDescriptionView.setText(productDescription != null ? productDescription : "No Description");
        productDetailsView.setText(productDetails != null ? productDetails : "No Details");

        Glide.with(this)
                .load(productImage)
                .placeholder(R.drawable.product_1)
                .error(R.drawable.starting_img)
                .into(productImageView);

        ImageView toCart = findViewById(R.id.toCart);
        toCart.setOnClickListener(view -> {
            Intent cartIntent = new Intent(this, Cart_activity.class);
            startActivity(cartIntent);
        });

        // Check if the product is already in the cart
        CartItem cartItem = cartManager.getCart().get(productId);

        // Update the button or show quantity layout
        updateUIState(addToCartButton, quantityLayout, quantityText, cartItem);

        // Add to Cart button logic
        addToCartButton.setOnClickListener(v -> {
            if (cartItem == null) {
                // Add the item to the cart
                cartManager.addToCart(new CartItem(productId, productTitle, productImage, productPrice, 1, productDescription, productDetails));
            } else if (cartItem.getQuantity() < 10) {
                // Increase the quantity if less than 10
                cartManager.updateQuantity(productId, cartItem.getQuantity() + 1);
            } else {
                Toast.makeText(this, "Maximum limit reached", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update UI after adding to cart
            CartItem updatedCartItem = cartManager.getCart().get(productId);
            updateUIState(addToCartButton, quantityLayout, quantityText, updatedCartItem);
            Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show();
        });

        // Increase quantity logic
        increaseButton.setOnClickListener(v -> {
            CartItem selectedCartItem = cartManager.getCart().get(productId);
            if (selectedCartItem != null && selectedCartItem.getQuantity() < 10) {
                cartManager.updateQuantity(productId, selectedCartItem.getQuantity() + 1);
                quantityText.setText(String.valueOf(selectedCartItem.getQuantity()));
            } else {
                Toast.makeText(this, "Maximum limit reached", Toast.LENGTH_SHORT).show();
            }
        });

        // Decrease quantity logic
        decreaseButton.setOnClickListener(v -> {
            CartItem selectedCartItem = cartManager.getCart().get(productId);
            if (selectedCartItem != null && selectedCartItem.getQuantity() > 1) {
                cartManager.updateQuantity(productId, selectedCartItem.getQuantity() - 1);
                quantityText.setText(String.valueOf(selectedCartItem.getQuantity()));
            } else if (selectedCartItem != null && selectedCartItem.getQuantity() == 1) {
                cartManager.removeFromCart(productId);
                updateUIState(addToCartButton, quantityLayout, quantityText, null);
                Toast.makeText(this, "Item removed from cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper method to update the Add to Cart button or show quantity controls
    private void updateUIState(Button addToCartButton, LinearLayout quantityLayout, TextView quantityText, CartItem cartItem) {
        if (cartItem == null || cartItem.getQuantity() == 0) {
            addToCartButton.setVisibility(View.VISIBLE);
            quantityLayout.setVisibility(View.GONE);
        } else {
            addToCartButton.setVisibility(View.GONE);
            quantityLayout.setVisibility(View.VISIBLE);
            quantityText.setText(String.valueOf(cartItem.getQuantity()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String productId = getIntent().getStringExtra("productId");

        CartItem cartItem = cartManager.getCart().get(productId);

        Button addToCartButton = findViewById(R.id.add_to_cart_button);
        LinearLayout quantityLayout = findViewById(R.id.quantity_layout);
        TextView quantityText = findViewById(R.id.quantity_text);

        updateUIState(addToCartButton, quantityLayout, quantityText, cartItem);
    }
}
