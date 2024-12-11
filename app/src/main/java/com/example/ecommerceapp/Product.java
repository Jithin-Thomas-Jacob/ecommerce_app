package com.example.ecommerceapp;

public class Product {

    private String id;
    private String title;
    private double price;
    private String image;
    private boolean inCart;
    private int quantity;
    private String description;
    private String itemDetails;

    public Product() {}

    public Product(String id, String title, double price, String image, String description, String itemDetails){
        this.id = id;
        this.title = title;
        this.price = price;
        this.image = image;
        this.inCart = false;
        this.quantity = 0;
        this.itemDetails = itemDetails;
        this.description = description;
    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }
    public double getPrice(){
        return price;
    }
    public String getImage(){
        return image;
    }

    public String getDescription(){
        return description;
    }

    public String getItemDetails(){
        return itemDetails;
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
