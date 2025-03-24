package com.example.yourmealapp;

import android.content.Intent;
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

import com.example.yourmealapp.models.User;

public class MainActivity extends AppCompatActivity {

    EditText fullname, email, phone, username, password, repassword;
    Button btnSignup, btnSignin;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Khai báo View
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        btnSignin = findViewById(R.id.btnSignin);
        btnSignup = findViewById(R.id.btnSignup);

        // Khởi tạo DB
        DB = new DBHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Sự kiện Click Đăng Nhập
        btnSignin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        // Sự kiện Click Đăng Ký
        btnSignup.setOnClickListener(v -> {
            String name = fullname.getText().toString().trim();
            String mail = email.getText().toString().trim();
            String phoneNum = phone.getText().toString().trim();
            String user_name = username.getText().toString().trim();
            String pass = password.getText().toString();
            String repass = repassword.getText().toString();

            // Kiểm tra dữ liệu nhập vào
            if (name.isEmpty() || mail.isEmpty() || phoneNum.isEmpty() || user_name.isEmpty() || pass.isEmpty() || repass.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(repass)) {
                Toast.makeText(MainActivity.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra tài khoản đã tồn tại chưa
            if (DB.checkUsername(user_name)) {
                Toast.makeText(MainActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Thêm tài khoản mới
            User user = new User(user_name, pass, name, mail, phoneNum, "user");
            boolean isRegistered = DB.registerUser(user);
            if (isRegistered) {
                Toast.makeText(MainActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Đăng ký thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
