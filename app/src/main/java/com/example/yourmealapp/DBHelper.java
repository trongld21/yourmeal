package com.example.yourmealapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.yourmealapp.models.Category;
import com.example.yourmealapp.models.Meal;
import com.example.yourmealapp.models.MealDistance;
import com.example.yourmealapp.models.Restaurant;
import com.example.yourmealapp.models.User;
import com.example.yourmealapp.models.UserMealHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.stream.Collectors;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "YourMeal.db";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, password TEXT NOT NULL, fullname TEXT NOT NULL, email TEXT NOT NULL UNIQUE, phone TEXT NOT NULL, role TEXT CHECK(role IN ('user', 'admin')) NOT NULL)");
        db.execSQL("CREATE TABLE categories(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE)");
        db.execSQL("CREATE TABLE meals(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, category_id INTEGER NOT NULL, main_ingredient TEXT NOT NULL, FOREIGN KEY (category_id) REFERENCES categories(id))");
        db.execSQL("CREATE TABLE user_meal_history(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, meal_id INTEGER NOT NULL, date TEXT NOT NULL, FOREIGN KEY (username) REFERENCES users(username), FOREIGN KEY (meal_id) REFERENCES meals(id))");
        db.execSQL("CREATE TABLE IF NOT EXISTS favorite_meals(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "meal_id INTEGER, " +
                "FOREIGN KEY(username) REFERENCES users(username), " +
                "FOREIGN KEY(meal_id) REFERENCES meals(id))");

        // Insert tài khoản admin
        db.execSQL("INSERT OR IGNORE INTO users (username, password, fullname, email, phone, role) " +
                "VALUES ('admin', '123456', 'Administrator', 'admin@yourmeal.com', '0123456789', 'admin')");

        // Insert tài khoản user
        db.execSQL("INSERT OR IGNORE INTO users (username, password, fullname, email, phone, role) " +
                "VALUES ('user', '123', 'User', 'user@yourmeal.com', '0123456789', 'user')");

        // Insert danh mục món ăn
        db.execSQL("INSERT OR IGNORE INTO categories (name) VALUES " +
                "('Món chính'), " +
                "('Món phụ'), " +
                "('Món tráng miệng'), " +
                "('Món chay'), " +
                "('Hải sản'), " +
                "('Đồ nướng'), " +
                "('Món nước'), " +
                "('Món Âu'), " +
                "('Món Á'), " +
                "('Thức ăn nhanh')");

        // Tạo sẵn 100 món ăn khác nhau
        db.execSQL("INSERT INTO meals (name, category_id, main_ingredient) VALUES " +
                "('Phở bò', 7, 'Thịt bò'), " +
                "('Bánh mì', 10, 'Bột mì'), " +
                "('Cơm tấm', 1, 'Sườn nướng'), " +
                "('Bún bò Huế', 7, 'Thịt bò'), " +
                "('Gỏi cuốn', 2, 'Tôm'), " +
                "('Bánh xèo', 6, 'Tôm và thịt'), " +
                "('Chè bưởi', 3, 'Bưởi'), " +
                "('Lẩu hải sản', 5, 'Hải sản'), " +
                "('Mì Quảng', 7, 'Thịt gà'), " +
                "('Bánh flan', 3, 'Trứng gà'), " +
                "('Bánh tráng trộn', 2, 'Bánh tráng'), " +
                "('Gà nướng', 6, 'Thịt gà'), " +
                "('Lẩu gà lá é', 1, 'Thịt gà'), " +
                "('Sushi', 9, 'Cá hồi'), " +
                "('Pizza', 8, 'Phô mai'), " +
                "('Mỳ Ý', 8, 'Thịt bò băm'), " +
                "('Bò bít tết', 8, 'Thịt bò'), " +
                "('Gà rán', 10, 'Thịt gà'), " +
                "('Bánh bao', 2, 'Bột mì'), " +
                "('Bún chả', 1, 'Thịt lợn'), " +
                "('Bún riêu', 7, 'Cua'), " +
                "('Hủ tiếu', 7, 'Thịt heo'), " +
                "('Nem lụi', 6, 'Thịt heo'), " +
                "('Ốc len xào dừa', 5, 'Ốc len'), " +
                "('Chè đậu xanh', 3, 'Đậu xanh'), " +
                "('Kem dừa', 3, 'Dừa'), " +
                "('Canh chua cá lóc', 1, 'Cá lóc'), " +
                "('Bánh khọt', 6, 'Tôm'), " +
                "('Xôi gấc', 2, 'Gấc'), " +
                "('Bánh cuốn', 2, 'Bột gạo'), " +
                "('Bún thịt nướng', 1, 'Thịt heo nướng'), " +
                "('Nộm bò khô', 2, 'Thịt bò khô'), " +
                "('Bánh bèo', 2, 'Tôm cháy'), " +
                "('Cháo lòng', 7, 'Lòng heo'), " +
                "('Gỏi gà', 2, 'Thịt gà'), " +
                "('Lẩu bò', 1, 'Thịt bò'), " +
                "('Cá kho tộ', 1, 'Cá lóc'), " +
                "('Bánh canh cua', 7, 'Cua'), " +
                "('Bánh gạo cay (Tteokbokki)', 9, 'Bánh gạo'), " +
                "('Kimchi', 9, 'Cải thảo'), " +
                "('Lẩu nấm chay', 4, 'Nấm'), " +
                "('Cơm chay', 4, 'Đậu hũ'), " +
                "('Bánh da lợn', 3, 'Bột năng'), " +
                "('Chè trôi nước', 3, 'Bột nếp'), " +
                "('Bánh pía', 3, 'Sầu riêng'), " +
                "('Sườn xào chua ngọt', 1, 'Sườn heo'), " +
                "('Bò kho', 1, 'Thịt bò'), " +
                "('Canh khổ qua nhồi thịt', 1, 'Khổ qua'), " +
                "('Thịt kho tàu', 1, 'Thịt ba chỉ'), " +
                "('Bánh trung thu', 3, 'Đậu xanh'), " +
                "('Bánh bông lan', 3, 'Bột mì'), " +
                "('Bánh chuối hấp', 3, 'Chuối'), " +
                "('Lẩu thái', 5, 'Hải sản'), " +
                "('Hàu nướng mỡ hành', 5, 'Hàu'), " +
                "('Bánh mì chảo', 10, 'Thịt bò'), " +
                "('Trà sữa', 3, 'Trà'), " +
                "('Nước ép cam', 3, 'Cam'), " +
                "('Cà phê sữa đá', 3, 'Cà phê'), " +
                "('Nem rán', 6, 'Thịt heo'), " +
                "('Bánh ít trần', 2, 'Đậu xanh'), " +
                "('Lẩu dê', 1, 'Thịt dê'), " +
                "('Bánh khoai mì', 3, 'Khoai mì'), " +
                "('Chè thái', 3, 'Trái cây'), " +
                "('Bánh bột lọc', 2, 'Tôm'), " +
                "('Gỏi bò tái chanh', 2, 'Thịt bò'), " +
                "('Cút lộn xào me', 5, 'Trứng cút'), " +
                "('Cháo ếch Singapore', 7, 'Ếch'), " +
                "('Bún cá', 7, 'Cá'), " +
                "('Bánh hỏi', 2, 'Thịt heo quay'), " +
                "('Hột vịt lộn', 2, 'Trứng vịt'), " +
                "('Bánh tro', 3, 'Bột nếp'), " +
                "('Bún mắm', 7, 'Hải sản'), " +
                "('Lẩu chua cá bớp', 1, 'Cá bớp'), " +
                "('Bún đậu mắm tôm', 1, 'Đậu hũ'), " +
                "('Bánh giò', 2, 'Bột gạo'), " +
                "('Cháo gà', 7, 'Thịt gà'), " +
                "('Bánh nậm', 2, 'Tôm cháy'), " +
                "('Mì cay', 9, 'Mì gói'), " +
                "('Canh bí đỏ', 4, 'Bí đỏ'), " +
                "('Canh rau ngót', 4, 'Rau ngót'), " +
                "('Bánh cam', 3, 'Bột nếp'), " +
                "('Chè khoai môn', 3, 'Khoai môn'), " +
                "('Bánh patê sô', 8, 'Thịt xay'), " +
                "('Cơm rang dưa bò', 1, 'Thịt bò'), " +
                "('Bánh chưng', 3, 'Đậu xanh và thịt heo'), " +
                "('Xôi gà', 2, 'Thịt gà'), " +
                "('Bún bò Nam Bộ', 1, 'Thịt bò'), " +
                "('Canh chua cá hồi', 1, 'Cá hồi'), " +
                "('Nem chua', 6, 'Thịt heo'), " +
                "('Xíu mại', 2, 'Thịt heo băm'), " +
                "('Bánh rán đường', 3, 'Bột nếp')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS meals");
        db.execSQL("DROP TABLE IF EXISTS restaurants");
        db.execSQL("DROP TABLE IF EXISTS user_meal_history");
        onCreate(db);
    }

    // Authentication
    public boolean registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("fullname", user.getFullname());
        values.put("email", user.getEmail());
        values.put("phone", user.getPhone());
        values.put("role", user.getRole());
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

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);
        if (cursor.moveToFirst()) {
            do {
                users.add(new User(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    public List<Meal> getUserMealHistory(String username) {
        List<Meal> history = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT m.id, m.name, m.category_id, m.main_ingredient " +
                        "FROM user_meal_history h " +
                        "JOIN meals m ON h.meal_id = m.id " +
                        "WHERE h.username = ? " +
                        "ORDER BY h.date DESC", new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                history.add(new Meal(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return history;
    }

    public Meal suggestMeal(String username, int k) {
        List<Meal> history = getUserMealHistory(username);
        if (history.isEmpty()) {
            return getRandomMeal(); // Nếu chưa có lịch sử, chọn ngẫu nhiên
        }

        List<Meal> allMeals = getAllMeals();
        List<MealDistance> distances = new ArrayList<>();

        // Tính khoảng cách giữa các món ăn
        for (Meal meal : allMeals) {
            double totalDistance = 0;
            for (Meal pastMeal : history) {
                totalDistance += calculateDistance(meal, pastMeal);
            }
            totalDistance /= history.size(); // Trung bình khoảng cách
            distances.add(new MealDistance(meal, totalDistance));
        }

        // Sắp xếp danh sách theo khoảng cách tăng dần
        distances.sort(Comparator.comparingDouble(MealDistance::getDistance));

        // 📌 Lọc ra các món đã chọn trước đó
        List<MealDistance> filteredMeals = distances.stream()
                .filter(md -> !history.contains(md.getMeal())) // Loại bỏ món đã ăn
                .collect(Collectors.toList());

        if (filteredMeals.isEmpty()) {
            return getRandomMeal(); // Nếu không còn món nào hợp lệ, chọn ngẫu nhiên
        }

        // 📌 Chọn ngẫu nhiên 1 trong k món gần nhất để tránh lặp lại liên tục
        Random random = new Random();
        int randomIndex = random.nextInt(Math.min(k, filteredMeals.size()));

        return filteredMeals.get(randomIndex).getMeal();
    }

    public double calculateDistance(Meal meal1, Meal meal2) {
        double categoryDiff = meal1.getCategoryId() - meal2.getCategoryId();

        double ingredientDiff = meal1.getMainIngredient().equals(meal2.getMainIngredient()) ? 0 : 1;

        return Math.sqrt(categoryDiff * categoryDiff + ingredientDiff * ingredientDiff);
    }


    private Meal getMostFrequentMeal(PriorityQueue<MealDistance> nearestMeals) {
        Map<Meal, Integer> countMap = new HashMap<>();
        for (MealDistance md : nearestMeals) {
            countMap.put(md.getMeal(), countMap.getOrDefault(md.getMeal(), 0) + 1);
        }

        return Collections.max(countMap.entrySet(), Map.Entry.comparingByValue()).getKey();
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

    public Meal getRandomMeal() {
        SQLiteDatabase db = this.getReadableDatabase();
        Meal randomMeal = null;

        Cursor cursor = db.rawQuery(
                "SELECT id, name, category_id, main_ingredient FROM meals ORDER BY RANDOM() LIMIT 1",
                null
        );

        if (cursor.moveToFirst()) {
            randomMeal = new Meal(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getString(3)
            );
        }
        cursor.close();
        return randomMeal;
    }

    public Cursor getRandomRestaurant() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM restaurants ORDER BY RANDOM() LIMIT 1", null);
    }

    public boolean saveMealHistory(String username, int mealId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("meal_id", mealId);
        values.put("date", date);

        long result = db.insert("user_meal_history", null, values);
        return result != -1;
    }

//    public List<UserMealHistory> getMealHistory(String username) {
//        List<UserMealHistory> historyList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.rawQuery("SELECT * FROM user_meal_history WHERE username = ? ORDER BY date DESC",
//                new String[]{username});
//
//        if (cursor.moveToFirst()) {
//            do {
//                int id = cursor.getInt(cursor.getColumnIndex("id"));
//                int mealId = cursor.getInt(cursor.getColumnIndex("meal_id"));
//                int restaurantId = cursor.getInt(cursor.getColumnIndex("restaurant_id"));
//                String date = cursor.getString(cursor.getColumnIndex("date"));
//
//                historyList.add(new UserMealHistory(id, username, mealId, restaurantId, date));
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        db.close();
//        return historyList;
//    }

    // Admin Functions:

    // Meals
    public boolean addMeal(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", meal.getName());
        values.put("category_id", meal.getCategoryId());
        values.put("main_ingredient", meal.getMainIngredient());

        long result = db.insert("meals", null, values);
        return result != -1;
    }

    public boolean updateMeal(int id, String name, int categoryId, String ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("category_id", categoryId);
        values.put("main_ingredient", ingredient);

        int result = db.update("meals", values, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteMeal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM meals", null);
        int mealCount = 0;
        if (cursor.moveToFirst()) {
            mealCount = cursor.getInt(0);
        }
        cursor.close();

        if (mealCount <= 9) {
            return false;
        }

        int result = db.delete("meals", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public ArrayList<Meal> getAllMeals() {
        ArrayList<Meal> meals = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM meals", null);

        if (cursor.moveToFirst()) {
            do {
                meals.add(new Meal(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return meals;
    }

    // Categories
    public boolean addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        long result = db.insert("categories", null, values);
        return result != -1;
    }

    public boolean updateCategory(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);

        int result = db.update("categories", values, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("categories", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM categories", null);
        if (cursor.moveToFirst()) {
            do {
                categories.add(new Category(
                        cursor.getInt(0),
                        cursor.getString(1)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    public int getCategoryIdByName(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM categories WHERE name = ?", new String[]{categoryName});
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }

    // Restaurant
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

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM restaurants", null);

        if (cursor.moveToFirst()) {
            do {
                restaurants.add(new Restaurant(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return restaurants;
    }

    public HashMap<String, String> getUserInfo(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT username, fullname, email, phone, role FROM users WHERE username = ?",
                new String[]{username}
        );

        HashMap<String, String> userInfo = null;
        if (cursor.moveToFirst()) {
            userInfo = new HashMap<>();
            userInfo.put("username", cursor.getString(0));
            userInfo.put("fullname", cursor.getString(1));
            userInfo.put("email", cursor.getString(2));
            userInfo.put("phone", cursor.getString(3));
            userInfo.put("role", cursor.getString(4));
        }

        cursor.close();
        return userInfo;
    }

    public boolean updateUserInfo(String username, String fullname, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullname", fullname);
        values.put("email", email);
        values.put("phone", phone);

        int result = db.update("users", values, "username = ?", new String[]{username});
        return result > 0;
    }

    public boolean addFavoriteMeal(String username, int mealId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("meal_id", mealId);

        // Kiểm tra trùng
        Cursor cursor = db.rawQuery("SELECT * FROM favorite_meals WHERE username = ? AND meal_id = ?", new String[]{username, String.valueOf(mealId)});
        if (cursor.moveToFirst()) {
            cursor.close();
            return false; // đã tồn tại
        }

        cursor.close();
        long result = db.insert("favorite_meals", null, values);
        return result != -1;
    }

    public List<Meal> getFavoriteMeals(String username) {
        List<Meal> favorites = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT m.id, m.name, m.category_id, m.main_ingredient " +
                        "FROM favorite_meals f JOIN meals m ON f.meal_id = m.id WHERE f.username = ?",
                new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                favorites.add(new Meal(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return favorites;
    }

    public boolean removeFavoriteMeal(String username, int mealId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete("favorite_meals", "username = ? AND meal_id = ?", new String[]{username, String.valueOf(mealId)});
        return deletedRows > 0;
    }


}
