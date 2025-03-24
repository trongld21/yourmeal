package com.example.yourmealapp.adapters;
import com.example.yourmealapp.DBHelper;
import com.example.yourmealapp.R;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yourmealapp.R;
import com.example.yourmealapp.models.Meal;
import java.util.ArrayList;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private ArrayList<Meal> mealList;
    private MealAdapterListener listener;

    public MealAdapter(ArrayList<Meal> mealList, MealAdapterListener listener) {
        this.mealList = mealList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        Log.d("MealAdapter", "Binding món ăn: " + meal.getName());

        holder.mealName.setText(meal.getName());
        holder.mealIngredient.setText("Nguyên liệu chính: " + meal.getMainIngredient());

        holder.btnMoreOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.btnMoreOptions);
            popupMenu.inflate(R.menu.meal_options_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_edit) {
                    showEditDialog(v, meal);
                    return true;
                } else if (item.getItemId() == R.id.action_delete) {
                    showDeleteDialog(v, meal);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        Log.d("MealAdapter", "Số lượng món ăn: " + mealList.size());
        return mealList.size();
    }

    public void updateData(ArrayList<Meal> newMeals) {
        Log.d("MealAdapter", "Cập nhật danh sách món ăn, số lượng: " + newMeals.size());

        mealList.clear();
        mealList.addAll(newMeals);
        notifyDataSetChanged();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealName, mealIngredient;
        ImageButton btnMoreOptions;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.textMealName);
            mealIngredient = itemView.findViewById(R.id.textMealIngredient);
            btnMoreOptions = itemView.findViewById(R.id.btnMoreOptions); // Ánh xạ nút 3 chấm
        }
    }

    public void deleteMeal(Meal meal) {
        mealList.remove(meal);
        notifyDataSetChanged();
    }

    private void showEditDialog(View view, Meal meal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Sửa món ăn");

        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.fragment_edit_meal_dialog, null);
        EditText editMealName = dialogView.findViewById(R.id.editMealName);
        EditText editMealIngredient = dialogView.findViewById(R.id.editMealIngredient);

        editMealName.setText(meal.getName());
        editMealIngredient.setText(meal.getMainIngredient());

        builder.setView(dialogView);

        builder.setPositiveButton("Cập nhật", null); // Gán sau để tránh auto-dismiss
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String newName = editMealName.getText().toString().trim();
            String newIngredient = editMealIngredient.getText().toString().trim();

            if (newName.isEmpty() || newIngredient.isEmpty()) {
                Toast.makeText(view.getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            DBHelper dbHelper = new DBHelper(view.getContext());
            Log.d("MealAdapter", "Meal ID cần cập nhật: " + meal.getId());
            boolean isUpdated = dbHelper.updateMeal(meal.getId(), newName, meal.getCategoryId(), newIngredient);
            if (isUpdated) {
                meal.setName(newName);
                meal.setMainIngredient(newIngredient);
                notifyDataSetChanged();
                Toast.makeText(view.getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(view.getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteDialog(View view, Meal meal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa món ăn này?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            DBHelper dbHelper = new DBHelper(view.getContext());
            boolean isDeleted = dbHelper.deleteMeal(meal.getId());

            if (isDeleted) {
                mealList.remove(meal);
                notifyDataSetChanged();
                Toast.makeText(view.getContext(), "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), "Xóa thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}

