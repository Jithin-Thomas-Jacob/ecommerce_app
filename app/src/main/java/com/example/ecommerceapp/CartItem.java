package com.example.ecommerceapp;

public class CartItem {

    private String Id;
    private String title;
    private int price;
    private String image;
    private int quantity;

    public CartItem(String Id ,String title, String image, int price, int quantity){
        this.Id = Id;
        this.title = title;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
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

    public int getPrice(){
        return price;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
}
