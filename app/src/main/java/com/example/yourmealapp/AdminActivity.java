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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button btnManageMeals = findViewById(R.id.btnManageMeals);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Load mặc định là trang Quản lý Món Ăn
        loadFragment(new ManageMealsFragment());

        btnManageMeals.setOnClickListener(view -> loadFragment(new ManageMealsFragment()));
        btnLogout.setOnClickListener(view -> {
            // Xử lý đăng xuất (chuyển về LoginActivity)
            finish();
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}