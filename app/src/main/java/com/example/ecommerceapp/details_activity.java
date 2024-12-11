package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
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

        // Initialize the CartManager singleton
        cartManager = CartManager.getInstance();

        // Retrieve product details from the Intent
        String productId = getIntent().getStringExtra("productId");
        String productTitle = getIntent().getStringExtra("productTitle");
        String productDescription = getIntent().getStringExtra("productDescription");
        double productPrice = getIntent().getDoubleExtra("productPrice", 0.00);
        String productImage = getIntent().getStringExtra("productImage");

        // Find views
        ImageView productImageView = findViewById(R.id.product_image);
        TextView productTitleView = findViewById(R.id.product_title);
        TextView productPriceView = findViewById(R.id.product_price);
        TextView productDescriptionView = findViewById(R.id.product_description);
        Button addToCartButton = findViewById(R.id.add_to_cart_button);
        Button buyNowButton = findViewById(R.id.buy_now_button);

        // Populate UI with product details
        productTitleView.setText(productTitle != null ? productTitle : "No Title");
        productPriceView.setText("Price: $" + String.format("%.2f", productPrice));
        productDescriptionView.setText(productDescription != null ? productDescription : "No Description");

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
        final CartItem cartItem = cartManager.getCart().get(productId);

        // Update the button state
        updateCartButtonState(addToCartButton, cartItem);

        // Add to Cart button logic
        addToCartButton.setOnClickListener(v -> {
            if (cartItem == null) {
                // Add the item to the cart
                cartManager.addToCart(new CartItem(productId, productTitle, productImage, productPrice, 1));
            } else if (cartItem.getQuantity() < 10) {
                // Increase the quantity if less than 10
                cartManager.updateQuantity(productId, cartItem.getQuantity() + 1);
            } else {
                // Display a message if the maximum limit is reached
                Toast.makeText(this, "Maximum limit reached", Toast.LENGTH_SHORT).show();
            }
            // Update the button state after changes
            updateCartButtonState(addToCartButton, cartItem);
            Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show();
        });

        // Buy Now button logic
        buyNowButton.setOnClickListener(v -> {
            // Display a message for now, implement Buy Now functionality later

        });
    }

    // Helper method to update the Add to Cart button text
    private void updateCartButtonState(Button addToCartButton, CartItem cartItem) {
        if (cartItem == null || cartItem.getQuantity() == 0) {
            addToCartButton.setText("Add to Cart");
        } else {
            addToCartButton.setText("In Cart (" + cartItem.getQuantity() + ")");
        }
    }
}