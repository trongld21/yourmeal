package com.example.yourmealapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ManageMealsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_meals, container, false);

        Button btnShowMeals = view.findViewById(R.id.btnShowMeals);
        Button btnAddMeal = view.findViewById(R.id.btnAddMeal);

        btnShowMeals.setOnClickListener(v -> replaceFragment(new MealListFragment()));
        btnAddMeal.setOnClickListener(v -> replaceFragment(new AddMealFragment()));

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.mealFragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}