package com.example.houselocation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.houselocation.models.House;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "houseLocationDB";
    private static final int DB_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT UNIQUE," +
                        "email TEXT UNIQUE," +
                        "password TEXT," +
                        "role TEXT DEFAULT 'user')"
        );

        db.execSQL(
                "CREATE TABLE houses (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "title TEXT," +
                        "description TEXT," +
                        "price INTEGER," +
                        "available INTEGER," +
                        "image TEXT)"
        );

        db.execSQL("INSERT INTO users(username,email,password,role) VALUES " +
                "('admin','admin@gmail.com','admin123','admin')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS houses");
        onCreate(db);
    }

    // USERS
    public boolean insertUser(String username, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("email", email);
        cv.put("password", password);
        cv.put("role", role);
        return db.insert("users", null, cv) != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{username, password});
        boolean ok = c.getCount() > 0;
        c.close();
        return ok;
    }

    public String getUserRole(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT role FROM users WHERE username=?", new String[]{username});
        if (c.moveToFirst()) {
            String role = c.getString(0);
            c.close();
            return role;
        }
        c.close();
        return "user";
    }

    // HOUSES
    public long insertHouse(String title, String description, int price, boolean available, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("description", description);
        cv.put("price", price);
        cv.put("available", available ? 1 : 0);
        cv.put("image", imageUri);
        return db.insert("houses", null, cv);
    }

    public boolean updateHouse(int id, String title, String description, int price, boolean available, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("description", description);
        cv.put("price", price);
        cv.put("available", available ? 1 : 0);
        cv.put("image", imageUri);
        return db.update("houses", cv, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteHouse(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("houses", "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public List<House> getAllHouses() {
        List<House> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM houses", null);
        while (c.moveToNext()) {
            list.add(new House(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getInt(3),
                    c.getInt(4) == 1,
                    c.getString(5)
            ));
        }
        c.close();
        return list;
    }

    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM users WHERE username=?", new String[]{username});
        boolean exists = c.getCount() > 0;
        c.close();
        return exists;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM users WHERE email=?", new String[]{email});
        boolean exists = c.getCount() > 0;
        c.close();
        return exists;
    }
}
