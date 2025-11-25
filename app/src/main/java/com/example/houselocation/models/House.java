package com.example.houselocation.models;
public class House {
    private int id;
    private String title;
    private String description;
    private double price;
    private boolean available;
    private String imageUri;

    public House(int id, String title, String description, double price, boolean available, String imageUri) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.available = available;
        this.imageUri = imageUri;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return available; }
    public String getImageUri() { return imageUri; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
}
