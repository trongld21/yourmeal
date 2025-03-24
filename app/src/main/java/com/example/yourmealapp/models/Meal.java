package com.example.yourmealapp.models;

public class Meal {
    private int id;
    private String name;
    private int categoryId;
    private String mainIngredient;

    public Meal(int id, String name, int categoryId, String mainIngredient) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.mainIngredient = mainIngredient;
    }

    public Meal(String name, int categoryId, String mainIngredient) {
        this.name = name;
        this.categoryId = categoryId;
        this.mainIngredient = mainIngredient;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getCategoryId() { return categoryId; }
    public String getMainIngredient() { return mainIngredient; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public void setMainIngredient(String mainIngredient) { this.mainIngredient = mainIngredient; }

    @Override
    public String toString() {
        return this.name;
    }
}
