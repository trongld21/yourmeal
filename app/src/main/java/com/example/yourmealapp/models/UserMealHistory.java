package com.example.yourmealapp.models;

public class UserMealHistory {
    private int id;
    private String username;
    private int mealId;
    private int restaurantId;
    private String date;

    public UserMealHistory(int id, String username, int mealId, int restaurantId, String date) {
        this.id = id;
        this.username = username;
        this.mealId = mealId;
        this.restaurantId = restaurantId;
        this.date = date;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public int getMealId() { return mealId; }
    public int getRestaurantId() { return restaurantId; }
    public String getDate() { return date; }
}
