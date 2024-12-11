package com.example.ecommerceapp;

public class CartItem {

    private String Id;
    private String title;
    private double price;
    private String image;
    private int quantity;
    private String description;
    private String itemDetails;

    public CartItem(String Id ,String title, String image, double price, int quantity, String description, String itemDetails){
        this.Id = Id;
        this.title = title;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.itemDetails = itemDetails;
    }

    public String getId(){
        return Id;
    }

    public String getTitle(){
        return title;
    }

    public String getImage(){
        return image;
    }

    public double getPrice(){
        return price;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public String getDescription(){
        return description;
    }

    public String getItemDetails(){
        return itemDetails;
    }
}
