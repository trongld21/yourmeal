package com.example.yourmealapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.yourmealapp.adapters.MealHistoryAdapter;
import com.example.yourmealapp.models.Meal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private TextView mealTextView;
    private ImageButton btnSuggestMeal, btnAcceptMeal, btnLogout;
    private RecyclerView historyRecyclerView;
    private MealHistoryAdapter mealHistoryAdapter;
    private DBHelper dbHelper;
    private String currentUsername;
    private Meal currentMeal;

    private RecyclerView favoriteRecyclerView;
    private MealHistoryAdapter favoriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        mealTextView = findViewById(R.id.mealTextView);
        mealTextView.setVisibility(View.VISIBLE);
        mealTextView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));

        btnSuggestMeal = findViewById(R.id.btnSuggestMeal);
        btnAcceptMeal = findViewById(R.id.btnAcceptMeal);
        btnLogout = findViewById(R.id.btnLogout);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
//        RecycleView hien thi danh sach yeu thich
        favoriteRecyclerView = findViewById(R.id.favoriteRecyclerView);


        dbHelper = new DBHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", null);

        if (currentUsername != null) {
            suggestMeal();
            loadMealHistory();
        } else {
            mealTextView.setText("Lỗi: Không lấy được thông tin user!");
        }

        setButtonEffect(btnSuggestMeal);
        setButtonEffect(btnAcceptMeal);

        btnSuggestMeal.setOnClickListener(v -> suggestMeal());
        btnAcceptMeal.setOnClickListener(v -> acceptMeal());
        btnLogout.setOnClickListener(v -> {
            showLogoutDialog();
        });

        loadFavoriteMeals();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    performLogout();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void performLogout() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit().clear().apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void suggestMeal() {
        if (currentUsername != null) {
            currentMeal = dbHelper.suggestMeal(currentUsername, 10);

            if (currentMeal != null) {
                mealTextView.setText("Gợi ý hôm nay: " + currentMeal.getName());
            } else {
                mealTextView.setText("Không có món ăn phù hợp!");
            }
        }
    }

    private void acceptMeal() {
        if (currentMeal != null) {
            boolean success = dbHelper.saveMealHistory(
                    currentUsername,
                    currentMeal.getId(),  // Lấy ID của món ăn
                    getCurrentDate()  // Lấy ngày hiện tại
            );

            if (success) {
                Toast.makeText(this, "Bạn đã chọn: " + currentMeal.getName(), Toast.LENGTH_SHORT).show();
                loadMealHistory();
            } else {
                Toast.makeText(this, "Lỗi khi lưu lịch sử món ăn!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Chưa có món ăn nào được chọn!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private Handler handler = new Handler();
    private int scrollPosition = 0;
    private Runnable autoScrollRunnable;

    private void loadMealHistory() {
        List<Meal> mealHistory = dbHelper.getUserMealHistory(currentUsername);
        mealHistoryAdapter = new MealHistoryAdapter(mealHistory, this, currentUsername, false, () -> loadFavoriteMeals());

        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        historyRecyclerView.setAdapter(mealHistoryAdapter);
        startAutoScroll(mealHistoryAdapter);


    }

    private void startAutoScroll(MealHistoryAdapter mealHistoryAdapter) {
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (scrollPosition < mealHistoryAdapter.getItemCount()) {
                    historyRecyclerView.smoothScrollToPosition(scrollPosition);
                    scrollPosition++;
                } else {
                    scrollPosition = 0;
                }
                handler.postDelayed(this, 3000); // Lặp lại sau 3 giây
            }
        };
        handler.postDelayed(autoScrollRunnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(autoScrollRunnable);
    }

    private void setButtonEffect(ImageButton button) {
        button.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_effect));
            }
            return false;
        });
    }

    public void userInfo(View view) {
//      Get user name from sharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        if (username != null) {
            // Gửi username sang màn hình thông tin người dùng
            Intent intent = new Intent(HomeActivity.this, UserProfile.class);
            intent.putExtra("username", username);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
        }
    }

//    Load favorite list
    private void loadFavoriteMeals() {
        List<Meal> favorites = dbHelper.getFavoriteMeals(currentUsername);
        favoriteAdapter = new MealHistoryAdapter(favorites, this, currentUsername, true, null);

        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        favoriteRecyclerView.setAdapter(favoriteAdapter);
    }

}
