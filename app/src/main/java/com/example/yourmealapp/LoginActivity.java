package com.example.yourmealapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        EditText username, password;
        Button btnSignin, btnSignup;

        // Setup EditText
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        // Setup Button
        btnSignin = (Button) findViewById(R.id.btnSignin);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        // Setup database
        DB = new DBHelper(this);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Cần điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    String role = DB.loginUser(user, pass);
                    if (role != null) {

                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", user);
                        editor.apply();

                        if (role.equals("admin")) {
                            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }
}