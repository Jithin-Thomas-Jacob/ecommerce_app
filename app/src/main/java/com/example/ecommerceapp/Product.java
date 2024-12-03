package com.example.ecommerceapp;

public class Product {

    private String id;
    private String title;
    private int price;
    private String image;
    private boolean inCart;
    private int quantity;

    public Product() {}

    public Product(String id, String title, int price, String image){
        this.id = id;
        this.title = title;
        this.price = price;
        this.image = image;
        this.inCart = false;
        this.quantity = 0;
    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }
    public int getPrice(){
        return price;
    }
    public String getImage(){
        return image;
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
