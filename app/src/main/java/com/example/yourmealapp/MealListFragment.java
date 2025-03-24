package com.example.yourmealapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yourmealapp.adapters.MealAdapter;
import com.example.yourmealapp.adapters.MealAdapterListener;
import com.example.yourmealapp.models.Meal;

import java.util.ArrayList;

public class MealListFragment extends Fragment {
    private DBHelper dbHelper;
    private RecyclerView recyclerViewMeals;
    private MealAdapter mealAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_list, container, false);

        dbHelper = new DBHelper(getContext());

        recyclerViewMeals = view.findViewById(R.id.recyclerViewMeals);
        recyclerViewMeals.setLayoutManager(new LinearLayoutManager(getContext()));

        mealAdapter = new MealAdapter(new ArrayList<>(), new MealAdapterListener() {
            @Override
            public void onEditMeal(Meal meal) {
                showEditMealDialog(meal);
            }

            @Override
            public void onDeleteMeal(Meal meal) {
                showDeleteConfirmationDialog(meal);
            }
        });
        recyclerViewMeals.setAdapter(mealAdapter);
        recyclerViewMeals.setAdapter(mealAdapter);
        ArrayList<Meal> meals = getListMeals();
        mealAdapter.updateData(meals);

        return view;
    }
    private ArrayList<Meal> getListMeals() {
        return dbHelper.getAllMeals();
    }

    private void showDeleteConfirmationDialog(Meal meal) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận xoá")
                .setMessage("Bạn có chắc chắn muốn xoá " + meal.getName() + "?")
                .setPositiveButton("Xoá", (dialog, which) -> {
                    mealAdapter.deleteMeal(meal);
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
    private void showEditMealDialog(Meal meal) {
        EditMealDialogFragment dialog = new EditMealDialogFragment(meal, mealAdapter);
        dialog.show(getParentFragmentManager(), "EditMealDialog");
    }
}