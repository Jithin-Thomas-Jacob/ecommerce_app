package com.example.ecommerceapp;

public class Product {

    private String title;
    private int price;
    private String image;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    }

    public Product(String title, int price, String image){
        this.title = title;
        this.price = price;
        this.image = image;
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
}
