package com.example.yourmealapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourmealapp.R;
import com.example.yourmealapp.models.Meal;

import java.util.List;

public class MealHistoryAdapter extends RecyclerView.Adapter<MealHistoryAdapter.MealViewHolder> {
    private final List<Meal> mealList;

    public MealHistoryAdapter(List<Meal> mealList) {
        this.mealList = mealList;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_history, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.mealNameTextView.setText(meal.getName());
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealNameTextView;

        MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealNameTextView = itemView.findViewById(R.id.mealName);
        }
    }
}
