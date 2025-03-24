package com.example.yourmealapp.adapters;

import com.example.yourmealapp.models.Meal;

public interface MealAdapterListener {
    void onEditMeal(Meal meal);
    void onDeleteMeal(Meal meal);
}
