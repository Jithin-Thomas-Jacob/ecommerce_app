package com.example.ecommerceapp;

import android.util.Log;

import java.util.HashMap;

public class CartManager {

    private static CartManager instance;
    private HashMap<String, CartItem> cart;

    private CartManager(){
        cart = new HashMap<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null){
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(CartItem item) {
        String productId = item.getId();
        if (cart.containsKey(productId)) {
            // If product exists, just update the quantity
            CartItem existingItem = cart.get(productId);
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            // Add as a new item
            cart.put(productId, item);
            Log.d("CartManager", "Added new item to cart: " + item.getTitle());
        }
        Log.d("CartManager", "Cart contents: " + cart);
    }


    public void updateQuantity(String productId, int quantity) {
        if (cart.containsKey(productId)) {
            cart.get(productId).setQuantity(quantity);
        }
    }

    public void removeFromCart(String productId) {
        cart.remove(productId);
    }

    public HashMap<String, CartItem> getCart() {
        return cart;
    }
}

