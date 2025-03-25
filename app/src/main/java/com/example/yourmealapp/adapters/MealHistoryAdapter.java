package com.example.yourmealapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourmealapp.DBHelper;
import com.example.yourmealapp.R;
import com.example.yourmealapp.models.Meal;

import java.util.List;

public class MealHistoryAdapter extends RecyclerView.Adapter<MealHistoryAdapter.MealViewHolder> {
    private final List<Meal> mealList;
    private final Context context;
    private final String username;
    private final boolean isFavoriteList; // true nếu là danh sách yêu thích
    private final OnFavoriteChangedListener favoriteChangedListener;
    public MealHistoryAdapter(List<Meal> mealList, Context context, String username, boolean isFavoriteList, OnFavoriteChangedListener listener) {
        this.mealList = mealList;
        this.context = context;
        this.username = username;
        this.isFavoriteList = isFavoriteList;
        this.favoriteChangedListener = listener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_history, parent, false);
        return new MealViewHolder(view);
    }

    public interface OnFavoriteChangedListener {
        void onFavoriteChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.mealNameTextView.setText(meal.getName());

        if (isFavoriteList) {
            holder.imgFavorite.setImageResource(R.drawable.tymed); // trái tim đầy
        } else {
            holder.imgFavorite.setImageResource(R.drawable.tym);   // trái tim rỗng
        }

        DBHelper dbHelper = new DBHelper(context);

        holder.imgFavorite.setOnClickListener(v -> {
            if (isFavoriteList) {
                // Xoá khỏi yêu thích
                boolean removed = dbHelper.removeFavoriteMeal(username, meal.getId());
                if (removed) {
                    Toast.makeText(context, "Đã xoá khỏi yêu thích!", Toast.LENGTH_SHORT).show();
                    mealList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mealList.size());

                    if (favoriteChangedListener != null) {
                        favoriteChangedListener.onFavoriteChanged();
                    }
                }
            } else {
                // Thêm vào yêu thích
                boolean added = dbHelper.addFavoriteMeal(username, meal.getId());
                if (added) {
                    Toast.makeText(context, "Đã thêm \"" + meal.getName() + "\" vào yêu thích!", Toast.LENGTH_SHORT).show();

                    if (favoriteChangedListener != null) {
                        favoriteChangedListener.onFavoriteChanged();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealNameTextView;
        ImageView imgFavorite;

        MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealNameTextView = itemView.findViewById(R.id.mealName);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
        }
    }
}
