package com.example.yourmealapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class UserProfile extends AppCompatActivity {

    TextView tvUsername, tvRole;
    EditText etFullname, etEmail, etPhone;

    DBHelper dbHelper;
    String usernameFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Ánh xạ view
        tvUsername = findViewById(R.id.tvUsername);
        tvRole = findViewById(R.id.tvRole);
        etFullname = findViewById(R.id.etFullname);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);

        dbHelper = new DBHelper(this);

        // Nhận username từ Intent
        Intent intent = getIntent();
        usernameFromIntent = intent.getStringExtra("username");

        if (usernameFromIntent != null) {
            loadUserInfo(usernameFromIntent);
        } else {
            Toast.makeText(this, "Lỗi: Không có username!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserInfo(String username) {
        HashMap<String, String> userInfo = dbHelper.getUserInfo(username);

        if (userInfo != null) {
            tvUsername.setText("Tên đăng nhập: " + userInfo.get("username"));
            etFullname.setText(userInfo.get("fullname"));
            etEmail.setText(userInfo.get("email"));
            etPhone.setText(userInfo.get("phone"));
            tvRole.setText("Vai trò: " + userInfo.get("role"));
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
        }
    }

    // 📌 Gọi từ nút "Cập nhật thông tin"
    public void handleChangeInfo(View view) {
        String newFullname = etFullname.getText().toString().trim();
        String newEmail = etEmail.getText().toString().trim();
        String newPhone = etPhone.getText().toString().trim();

        if (newFullname.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean updated = dbHelper.updateUserInfo(usernameFromIntent, newFullname, newEmail, newPhone);

        if (updated) {
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    public void handleBackToHome(View view) {
        Intent intent = new Intent(UserProfile.this, HomeActivity.class);
        intent.putExtra("username", usernameFromIntent); // gửi lại nếu cần dùng
        startActivity(intent);
        finish(); // Đóng UserProfile để tránh vòng lặp khi bấm back
    }
}
