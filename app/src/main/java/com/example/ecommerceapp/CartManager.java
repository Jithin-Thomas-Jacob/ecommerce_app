package com.example.ecommerceapp;

import android.util.Log;

import java.util.HashMap;

public class CartManager {

    private static CartManager instance;
    private HashMap<String, CartItem> cart;
    private static final double TAX_RATE = 0.13;

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

            CartItem existingItem = cart.get(productId);
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {

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


    public double[] getCartPriceDetails() {
        double subTotal = 0.0;

        for (CartItem item : cart.values()) {
            subTotal += item.getPrice() * item.getQuantity();
        }

        double tax = subTotal * TAX_RATE;
        double total = subTotal + tax;

        return new double[]{subTotal, tax, total};
    }

    public void clearCart() {
        cart.clear();
        Log.d("CartManager", "Cart cleared after purchase.");
    }

}
