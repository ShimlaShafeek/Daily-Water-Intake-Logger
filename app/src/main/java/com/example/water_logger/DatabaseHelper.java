package com.example.water_logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WaterApp.db";
    private static final int DATABASE_VERSION = 6; // ✅ changed (increment)

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";

    // Water records table
    private static final String TABLE_WATER_RECORDS = "water_records";
    private static final String COLUMN_RECORD_ID = "id";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_RECORD_DATE = "date"; // ✅ NEW

    // Daily Summary table
    private static final String TABLE_DAILY_SUMMARY = "daily_summary";
    private static final String COLUMN_SUMMARY_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_IS_COMPLETED = "is_completed";

    private static final SimpleDateFormat YMD =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_PHONE + " TEXT, "
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // ✅ water_records now has a date column
        String CREATE_WATER_RECORDS_TABLE = "CREATE TABLE " + TABLE_WATER_RECORDS + " ("
                + COLUMN_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TIMESTAMP + " INTEGER, "
                + COLUMN_AMOUNT + " INTEGER, "
                + COLUMN_RECORD_DATE + " TEXT)";
        db.execSQL(CREATE_WATER_RECORDS_TABLE);

        String CREATE_DAILY_SUMMARY_TABLE = "CREATE TABLE " + TABLE_DAILY_SUMMARY + " ("
                + COLUMN_SUMMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DATE + " TEXT UNIQUE, "
                + COLUMN_IS_COMPLETED + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_DAILY_SUMMARY_TABLE);

        // ✅ Optional but helpful for speed:
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_water_records_date ON " + TABLE_WATER_RECORDS + "(" + COLUMN_RECORD_DATE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // ⚠️ Your current behavior drops and recreates all tables.
        // OK for development; data will be erased on upgrade.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS water_intake"); // old table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WATER_RECORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY_SUMMARY);
        onCreate(db);
    }

    // ---------- Users ----------
    public boolean insertUser(String name, String email, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM " + TABLE_USERS +
                        " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=? LIMIT 1",
                new String[]{email, password}
        );
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // ---------- Water Records ----------
    public void addWaterRecord(int amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        long ts = System.currentTimeMillis();
        String dateStr = YMD.format(new Date(ts)); // ✅ save yyyy-MM-dd

        ContentValues values = new ContentValues();
        values.put(COLUMN_TIMESTAMP, ts);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_RECORD_DATE, dateStr);
        db.insert(TABLE_WATER_RECORDS, null, values);
    }

    // ✅ NEW: total for today by date column (stable)
    public int getTodayTotalIntake() {
        SQLiteDatabase db = this.getReadableDatabase();
        String today = getTodayDateString();

        Cursor cursor = db.rawQuery(
                "SELECT COALESCE(SUM(" + COLUMN_AMOUNT + "), 0) " +
                        "FROM " + TABLE_WATER_RECORDS + " WHERE " + COLUMN_RECORD_DATE + " = ?",
                new String[]{today}
        );

        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        return total;
    }

    // ✅ If you want total for any date:
    public int getTotalForDate(String yyyyMmDd) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COALESCE(SUM(" + COLUMN_AMOUNT + "), 0) " +
                        "FROM " + TABLE_WATER_RECORDS + " WHERE " + COLUMN_RECORD_DATE + " = ?",
                new String[]{yyyyMmDd}
        );
        int total = 0;
        if (cursor.moveToFirst()) total = cursor.getInt(0);
        cursor.close();
        return total;
    }

    // ✅ Hourly intake for a day (still uses timestamp to group by hour, but filters by date)
    public int[] getHourlyIntakeForDay(long dateMillis) {
        int[] hourlyIntake = new int[24];
        SQLiteDatabase db = this.getReadableDatabase();

        String dayStr = YMD.format(new Date(dateMillis));

        Cursor cursor = db.query(
                TABLE_WATER_RECORDS,
                new String[]{COLUMN_TIMESTAMP, COLUMN_AMOUNT},
                COLUMN_RECORD_DATE + " = ?",
                new String[]{dayStr},
                null, null, null
        );

        Calendar cal = Calendar.getInstance();
        if (cursor.moveToFirst()) {
            do {
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP));
                int amount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));

                cal.setTimeInMillis(timestamp);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                if (hour >= 0 && hour < 24) {
                    hourlyIntake[hour] += amount;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return hourlyIntake;
    }

    // ✅ Daily intake for week (Mon-Sun) using date-based totals
    public int[] getDailyIntakeForWeek(long dateMillis) {
        int[] dailyIntake = new int[7];
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateMillis);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int i = 0; i < 7; i++) {
            String dayStr = YMD.format(cal.getTime());
            dailyIntake[i] = getTotalForDate(dayStr);
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return dailyIntake;
    }

    // ---------- Daily Summary ----------
    private String getTodayDateString() {
        return YMD.format(new Date());
    }

    public void markDayAsCompleted() {
        SQLiteDatabase db = this.getWritableDatabase();
        String today = getTodayDateString();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, today);
        values.put(COLUMN_IS_COMPLETED, 1);

        db.insertWithOnConflict(TABLE_DAILY_SUMMARY, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public boolean isDayCompleted() {
        SQLiteDatabase db = this.getReadableDatabase();
        String today = getTodayDateString();

        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_IS_COMPLETED +
                        " FROM " + TABLE_DAILY_SUMMARY +
                        " WHERE " + COLUMN_DATE + " = ?",
                new String[]{today}
        );

        boolean completed = false;
        if (cursor.moveToFirst()) {
            completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1;
        }
        cursor.close();
        return completed;
    }
}