package com.example.ecommerceapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Cart_activity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private TextView subTotalTextView, taxTextView, totalPriceTextView, emptyCartMessage;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

    private static final double TAX_RATE = 0.13; // 13% tax

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        subTotalTextView = findViewById(R.id.subTotalPrice);
        taxTextView = findViewById(R.id.tax);
        totalPriceTextView = findViewById(R.id.totalPrice);
        emptyCartMessage = findViewById(R.id.emptyCartMessage);

        cartItems = new ArrayList<>(CartManager.getInstance().getCart().values());
        Log.d("CartActivity", "Cart items: " + cartItems);

        cartAdapter = new CartAdapter(cartItems, this::updateTotalPrice);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        updateCartUI();
        updateTotalPrice();
    }

    private void updateCartUI() {
        if (cartItems.isEmpty()) {
            // Show empty cart message and hide RecyclerView
            emptyCartMessage.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
        } else {
            // Hide empty cart message and show RecyclerView
            emptyCartMessage.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
        }

        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double subTotal = 0.0;

        // Calculate subtotal
        for (CartItem item : cartItems) {
            subTotal += item.getPrice() * item.getQuantity();
        }

        // Calculate tax and total
        double tax = subTotal * TAX_RATE;
        double total = subTotal + tax;

        // Update UI
        subTotalTextView.setText(String.format("Sub Total: $%.2f", subTotal));
        taxTextView.setText(String.format("Tax: $%.2f", tax));
        totalPriceTextView.setText(String.format("Total: $%.2f", total));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh the cart items
        cartItems.clear();
        cartItems.addAll(CartManager.getInstance().getCart().values());
        Log.d("CartActivity", "Cart items: " + cartItems);

        // Notify adapter of data change
        cartAdapter.notifyDataSetChanged();

        // Update the total price
        updateCartUI();
        updateTotalPrice();
    }
}
