package com.example.yourmealapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "YourMeal.db";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, password TEXT NOT NULL, fullname TEXT NOT NULL, email TEXT NOT NULL UNIQUE, phone TEXT NOT NULL, role TEXT CHECK(role IN ('user', 'admin')) NOT NULL)");
        db.execSQL("CREATE TABLE meals(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, category TEXT NOT NULL, main_ingredient TEXT NOT NULL)");
        db.execSQL("CREATE TABLE restaurants(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, address TEXT NOT NULL, price_range TEXT NOT NULL, best_seller TEXT NOT NULL)");
        db.execSQL("CREATE TABLE user_meal_history(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, meal_id INTEGER NOT NULL, restaurant_id INTEGER NOT NULL, date TEXT NOT NULL, FOREIGN KEY (username) REFERENCES users(username), FOREIGN KEY (meal_id) REFERENCES meals(id), FOREIGN KEY (restaurant_id) REFERENCES restaurants(id))");

        // Insert tài khoản admin
        db.execSQL("INSERT OR IGNORE INTO users (username, password, fullname, email, phone, role) " +
                "VALUES ('admin', '!23456Qa', 'Administrator', 'admin@yourmeal.com', '0123456789', 'admin')");    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS meals");
        db.execSQL("DROP TABLE IF EXISTS restaurants");
        db.execSQL("DROP TABLE IF EXISTS user_meal_history");
        onCreate(db);
    }

    // Authentication
    public boolean registerUser(String username, String password, String fullname, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("fullname", fullname);
        values.put("email", email);
        values.put("phone", phone);
        values.put("role", "user"); // Luôn đăng ký dưới quyền "user"

        long result = db.insert("users", null, values);
        return result != -1;
    }

    public String loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM users WHERE username = ? AND password = ?", new String[]{username, password});

        if (cursor.moveToFirst()) {
            return cursor.getString(0); // Trả về role (user hoặc admin)
        }
        return null; // Đăng nhập thất bại
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // User Functions:
    public Cursor getTopMeals() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM meals LIMIT 5", null);
    }

    public Cursor getTopRestaurants() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM restaurants LIMIT 5", null);
    }

    public Cursor getRandomMeal() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM meals ORDER BY RANDOM() LIMIT 1", null);
    }

    public Cursor getRandomRestaurant() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM restaurants ORDER BY RANDOM() LIMIT 1", null);
    }

    public boolean saveMealHistory(String username, int mealId, int restaurantId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("meal_id", mealId);
        values.put("restaurant_id", restaurantId);
        values.put("date", date);

        long result = db.insert("user_meal_history", null, values);
        return result != -1;
    }

    // Admin Functions:
    public boolean addMeal(String name, String category, String ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("category", category);
        values.put("main_ingredient", ingredient);

        long result = db.insert("meals", null, values);
        return result != -1;
    }

    public boolean updateMeal(int id, String name, String category, String ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("category", category);
        values.put("main_ingredient", ingredient);

        int result = db.update("meals", values, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteMeal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("meals", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Cursor getAllMeals() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM meals", null);
    }

    public boolean addRestaurant(String name, String address, String priceRange, String bestSeller) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("price_range", priceRange);
        values.put("best_seller", bestSeller);

        long result = db.insert("restaurants", null, values);
        return result != -1;
    }

    public boolean updateRestaurant(int id, String name, String address, String priceRange, String bestSeller) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("price_range", priceRange);
        values.put("best_seller", bestSeller);

        int result = db.update("restaurants", values, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteRestaurant(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("restaurants", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Cursor getAllRestaurants() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM restaurants", null);
    }

}
