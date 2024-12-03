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

import java.util.ArrayList;
import java.util.List;

public class Cart_activity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private TextView totalPriceTextView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

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
        totalPriceTextView = findViewById(R.id.totalPrice);

        cartItems = new ArrayList<>(CartManager.getInstance().getCart().values());
        Log.d("CartActivity", "Cart items: " + cartItems);

        if (cartItems.isEmpty()) {
            Log.d("CartActivity", "Cart is empty!");
        } else {
            Log.d("CartActivity", "Cart contains items!");
        }

        cartAdapter = new CartAdapter(cartItems, this::updateTotalPrice);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        updateTotalPrice();
    }

    private void updateTotalPrice(){
        int totalPrice = 0;
        for(CartItem item: cartItems){
            totalPrice += item.getPrice() * item.getQuantity();
        }
        totalPriceTextView.setText("Total: $"+ totalPrice);
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
        updateTotalPrice();
    }

}