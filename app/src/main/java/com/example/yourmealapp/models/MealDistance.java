package com.example.yourmealapp.models;

public class MealDistance {
    private Meal meal;
    private double distance;

    public MealDistance(Meal meal, double distance) {
        this.meal = meal;
        this.distance = distance;
    }

    public Meal getMeal() {
        return meal;
    }

    public double getDistance() {
        return distance;
    }
}

