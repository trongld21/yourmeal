package com.example.yourmealapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourmealapp.models.Category;
import com.example.yourmealapp.models.Meal;

import java.util.ArrayList;

public class AddMealFragment extends Fragment {

    private DBHelper dbHelper;
    private EditText edtMealName, edtMealIngredient;
    private Spinner spinnerMealCategory;
    private Button btnSaveMeal;
    private ArrayList<Category> categoryList;
    private ArrayAdapter<Category> categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("AddMealFragment", "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_add_meal, container, false);

        dbHelper = new DBHelper(getContext());

        edtMealName = view.findViewById(R.id.edtMealName);
        spinnerMealCategory = view.findViewById(R.id.spinnerMealCategory);
        edtMealIngredient = view.findViewById(R.id.edtMealIngredient);
        btnSaveMeal = view.findViewById(R.id.btnSaveMeal);

        // Tải danh mục vào Spinner
        loadCategories();

        btnSaveMeal.setOnClickListener(v -> saveMeal());

        return view;
    }

    private void loadCategories() {
        categoryList = dbHelper.getAllCategories();
        categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view).setText(categoryList.get(position).getName());
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((TextView) view).setText(categoryList.get(position).getName());
                return view;
            }
        };
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealCategory.setAdapter(categoryAdapter);
    }

    private void saveMeal() {
        String mealName = edtMealName.getText().toString().trim();
        String mainIngredient = edtMealIngredient.getText().toString().trim();

        if (mealName.isEmpty() || mainIngredient.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy đối tượng Category từ Spinner
        Category selectedCategory = (Category) spinnerMealCategory.getSelectedItem();
        if (selectedCategory == null) {
            Toast.makeText(getContext(), "Danh mục không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        Meal meal = new Meal(mealName, selectedCategory.getId(), mainIngredient);
        boolean isInserted = dbHelper.addMeal(meal);
        if (isInserted) {
            Toast.makeText(getContext(), "Đã thêm: " + mealName, Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "Thêm thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
