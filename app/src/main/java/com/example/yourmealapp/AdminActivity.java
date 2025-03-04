package com.example.yourmealapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminActivity extends AppCompatActivity {
    Button btnManageMeals, btnManageRestaurants, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnManageMeals = findViewById(R.id.btnManageMeals);
        btnManageRestaurants = findViewById(R.id.btnManageRestaurants);
        btnLogout = findViewById(R.id.btnLogout);

        // Chuyển đến màn hình quản lý món ăn
        btnManageMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ManageMealsActivity.class);
                startActivity(intent);
            }
        });

        // Chuyển đến màn hình quản lý quán ăn
        btnManageRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ManageRestaurantsActivity.class);
                startActivity(intent);
            }
        });

        // Đăng xuất
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa lịch sử stack để tránh quay lại bằng nút back
                startActivity(intent);
                finish();
            }
        });
    }
}