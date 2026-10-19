package com.example.mediremind;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mediremind.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_MEDICINES = "medicines";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_QUANTITY = "quantity";
    private static final String COL_PRICE = "price";
    private static final String COL_EXPIRY = "expiry_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_MEDICINES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_QUANTITY + " INTEGER NOT NULL, " +
                COL_PRICE + " REAL NOT NULL, " +
                COL_EXPIRY + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINES);
        onCreate(db);
    }

    public long insertMedicine(Medicine medicine) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, medicine.getName());
        values.put(COL_QUANTITY, medicine.getQuantity());
        values.put(COL_PRICE, medicine.getPrice());
        values.put(COL_EXPIRY, medicine.getExpiryDate());

        return db.insert(TABLE_MEDICINES, null, values);
    }

    public ArrayList<Medicine> getMedicines(String searchText) {
        ArrayList<Medicine> medicines = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_MEDICINES,
                null,
                COL_NAME + " LIKE ?",
                new String[]{"%" + (searchText == null ? "" : searchText) + "%"},
                null,
                null,
                COL_ID + " DESC"
        );

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE));
                String expiry = cursor.getString(cursor.getColumnIndexOrThrow(COL_EXPIRY));

                medicines.add(new Medicine(id, name, quantity, price, expiry));
            }
        } finally {
            cursor.close();
        }

        return medicines;
    }

    public int getMedicineCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_MEDICINES, null);

        try {
            return cursor.moveToFirst() ? cursor.getInt(0) : 0;
        } finally {
            cursor.close();
        }
    }

    public int getLowStockCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_MEDICINES +
                        " WHERE " + COL_QUANTITY + " <= 10",
                null
        );

        try {
            return cursor.moveToFirst() ? cursor.getInt(0) : 0;
        } finally {
            cursor.close();
        }
    }

    public int updateQuantity(int id, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_QUANTITY, quantity);

        return db.update(
                TABLE_MEDICINES,
                values,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public int deleteMedicine(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(
                TABLE_MEDICINES,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }
}