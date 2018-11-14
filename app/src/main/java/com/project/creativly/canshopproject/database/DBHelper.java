package com.project.creativly.canshopproject.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.project.creativly.canshopproject.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;

    private static final String DATABASE_NAME = "Data.db";
    private static final String CART_TABLE_NAME = "Cart";
    private static final String CART_COLUMN_ID = "id";
    private static final String CART_COLUMN_PRODUCT_ID = "product_id";
    private static final String CART_COLUMN_IMAGE = "image";
    private static final String CART_COLUMN_PRICE = "price";
    private static final String CART_COLUMN_QUANTITY = "quantity";
    private static final String CART_COLUMN_NAME = "product_name";
    private static final String CART_COLUMN_DATE = "date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_TABLE = "CREATE TABLE " + CART_TABLE_NAME + "("
                + CART_COLUMN_ID + " INTEGER PRIMARY KEY,"
                + CART_COLUMN_PRODUCT_ID + " TEXT,"
                + CART_COLUMN_IMAGE + " TEXT,"
                + CART_COLUMN_PRICE + " TEXT,"
                + CART_COLUMN_QUANTITY + " TEXT,"
                + CART_COLUMN_DATE + " TEXT,"
                + CART_COLUMN_NAME + " TEXT" + ");";


        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CART_TABLE_NAME);
        onCreate(db);
    }

    public void insertCart(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CART_COLUMN_PRODUCT_ID, cart.getId());
        contentValues.put(CART_COLUMN_IMAGE, cart.getImage());
        contentValues.put(CART_COLUMN_PRICE, cart.getPrice());
        contentValues.put(CART_COLUMN_QUANTITY, cart.getQuantity());
        contentValues.put(CART_COLUMN_DATE, cart.getDate());
        contentValues.put(CART_COLUMN_NAME, cart.getTitle());
        db.insert(CART_TABLE_NAME, null, contentValues);
        Log.e("contentValues", contentValues + "");
        db.close();
    }


    public Cart getCart(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cart cart = new Cart();
        @SuppressLint("Recycle") Cursor cursor = db.query(CART_TABLE_NAME, new String[]{CART_COLUMN_PRODUCT_ID,
                        CART_COLUMN_IMAGE, CART_COLUMN_PRICE, CART_COLUMN_QUANTITY,
                        CART_COLUMN_DATE, CART_COLUMN_NAME},
                CART_COLUMN_PRODUCT_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            cart = new Cart(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5));
        }
        return cart;
    }

    public List<Cart> getAllCart() {
        List<Cart> cartList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + CART_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Cart user = new Cart();
                user.setId(cursor.getString(cursor.getColumnIndex(CART_COLUMN_PRODUCT_ID)));
                user.setImage(cursor.getString(cursor.getColumnIndex(CART_COLUMN_IMAGE)));
                user.setPrice(cursor.getString(cursor.getColumnIndex(CART_COLUMN_PRICE)));
                user.setQuantity(cursor.getString(cursor.getColumnIndex(CART_COLUMN_QUANTITY)));
                user.setDate(cursor.getString(cursor.getColumnIndex(CART_COLUMN_DATE)));
                user.setTitle(cursor.getString(cursor.getColumnIndex(CART_COLUMN_NAME)));
                cartList.add(user);
            } while (cursor.moveToNext());
        }
        db.close();
        // return contact list
        return cartList;
    }

    public List<Cart> getIdCart() {
        List<Cart> cartList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + CART_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Cart user = new Cart();
                user.setId(cursor.getString(cursor.getColumnIndex(CART_COLUMN_PRODUCT_ID)));
                cartList.add(user);
            } while (cursor.moveToNext());
        }
        db.close();
        // return contact list
        return cartList;
    }

    public int getCount(String id) {
        String countQuery = "SELECT * FROM " + CART_TABLE_NAME + " where " + CART_COLUMN_PRODUCT_ID + "=" + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public int getCartCount() {
        String countQuery = "SELECT * FROM " + CART_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public void deleteCart(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART_TABLE_NAME, CART_COLUMN_PRODUCT_ID + "=?",
                new String[]{id});
        db.close();
    }

    public void updateCart(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CART_COLUMN_PRODUCT_ID, cart.getId());
        contentValues.put(CART_COLUMN_IMAGE, cart.getImage());
        contentValues.put(CART_COLUMN_PRICE, cart.getPrice());
        contentValues.put(CART_COLUMN_QUANTITY, cart.getQuantity());
        contentValues.put(CART_COLUMN_DATE, cart.getDate());
        contentValues.put(CART_COLUMN_NAME, cart.getTitle());
        db.update(CART_TABLE_NAME, contentValues, CART_COLUMN_PRODUCT_ID + "=?",
                new String[]{cart.getId()});
        Log.e("contentValues", contentValues + "");
        db.close();
    }

}