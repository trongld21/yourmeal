package com.example.yourmealapp.models;

public class Restaurant {
    private int id;
    private String name;
    private String address;
    private String priceRange;
    private String bestSeller;

    public Restaurant(int id, String name, String address, String priceRange, String bestSeller) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.priceRange = priceRange;
        this.bestSeller = bestSeller;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPriceRange() { return priceRange; }
    public String getBestSeller() { return bestSeller; }
}

