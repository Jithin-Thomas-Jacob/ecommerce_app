package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Cart_activity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private TextView subTotalTextView, taxTextView, totalPriceTextView, emptyCartMessage;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private ImageView backButtonView;

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

        backButtonView = findViewById(R.id.backButton);
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        subTotalTextView = findViewById(R.id.subTotalPrice);
        taxTextView = findViewById(R.id.tax);
        totalPriceTextView = findViewById(R.id.totalPrice);
        emptyCartMessage = findViewById(R.id.emptyCartMessage);

        backButtonView.setOnClickListener(v -> {
            finish();
        });

        cartItems = new ArrayList<>(CartManager.getInstance().getCart().values());
        Log.d("CartActivity", "Cart items: " + cartItems);

        cartAdapter = new CartAdapter(cartItems, this::updateTotalPrice);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        updateCartUI();
        updateTotalPrice();

        Button checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(v -> {

            if (cartItems.isEmpty()) {
                Toast.makeText(Cart_activity.this, "Your cart is empty. Please add items to the cart before proceeding to checkout.", Toast.LENGTH_SHORT).show();
            } else {

                double[] priceDetails = CartManager.getInstance().getCartPriceDetails();
                double subTotal = priceDetails[0];
                double tax = priceDetails[1];
                double total = priceDetails[2];

                Intent intent = new Intent(Cart_activity.this, activity_checkout.class);
                intent.putExtra("subtotal_price", subTotal);
                intent.putExtra("tax_price", tax);
                intent.putExtra("total_price", total);
                startActivity(intent);
            }
        });
    }

    private void updateCartUI() {
        if (cartItems.isEmpty()) {

            emptyCartMessage.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
        } else {

            emptyCartMessage.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void updateTotalPrice() {

        double[] priceDetails = CartManager.getInstance().getCartPriceDetails();
        double subTotal = priceDetails[0];
        double tax = priceDetails[1];
        double total = priceDetails[2];


        subTotalTextView.setText(String.format("Sub Total: $%.2f", subTotal));
        taxTextView.setText(String.format("Tax: $%.2f", tax));
        totalPriceTextView.setText(String.format("Total: $%.2f", total));
    }

    @Override
    protected void onResume() {
        super.onResume();

        cartItems.clear();
        cartItems.addAll(CartManager.getInstance().getCart().values());
        Log.d("CartActivity", "Cart items: " + cartItems);

        cartAdapter.notifyDataSetChanged();

        updateCartUI();
        updateTotalPrice();
    }
}
