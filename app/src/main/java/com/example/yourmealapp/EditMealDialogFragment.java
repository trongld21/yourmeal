package com.example.yourmealapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yourmealapp.adapters.MealAdapter;
import com.example.yourmealapp.models.Meal;

public class EditMealDialogFragment extends DialogFragment {
    private Meal meal;
    private MealAdapter mealAdapter;

    public EditMealDialogFragment(Meal meal, MealAdapter mealAdapter) {
        this.meal = meal;
        this.mealAdapter = mealAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_meal_dialog, container, false);
        return view;
    }
}