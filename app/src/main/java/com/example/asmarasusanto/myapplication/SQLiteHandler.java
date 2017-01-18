package com.example.asmarasusanto.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by asmarasusanto on 1/7/17.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "epros";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IDUSER = "uid";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_AUTHKEY = "authkey";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_VEMAIL = "vemail";
    private static final String KEY_VSMS = "vsms";
    private static final String KEY_VBUKTI = "vbukti";
    private static final String KEY_VUPDATE = "vupdate";
    private static final String KEY_NGROUP = "nama";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ALAMAT = "alamat";
    private static final String KEY_BERDIRI = "tanggal";
    private static final String KEY_CABANG = "cabang";
    private static final String KEY_GAMBAR = "gambar";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_ACARA = "acara";
    private static final String KEY_MENERIMA = "menerima";
    private static final String KEY_JANGKAWAKTU = "jangka_waktu";
    private static final String KEY_TIMBAL_BALIK = "timbal_balik";
    private static final String KEY_SPONSOR = "sponsor";

    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IDUSER + " TEXT,"
            + KEY_USERNAME + " TEXT," + KEY_PASSWORD + " TEXT,"
            + KEY_AUTHKEY + " TEXT," + KEY_LEVEL + " TEXT,"
            + KEY_VEMAIL + " TEXT," + KEY_VSMS + " TEXT,"
            + KEY_VBUKTI + " TEXT," + KEY_VUPDATE + " TEXT,"
            + KEY_NGROUP + " TEXT," + KEY_PHONE + " TEXT,"
            + KEY_ALAMAT + " TEXT," + KEY_BERDIRI + " TEXT,"
            + KEY_CABANG + " TEXT," + KEY_GAMBAR + " TEXT,"
            + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT,"
            + KEY_ACARA + " TEXT," + KEY_MENERIMA + " TEXT,"
            + KEY_JANGKAWAKTU + " TEXT," + KEY_TIMBAL_BALIK + " TEXT,"
            + KEY_SPONSOR + " TEXT" + ")";

    private static final String TABLE_TEMAN = "teman";

    // Login Table Columns names
    private static final String KEY_TID = "id";
    private static final String KEY_TIDUSER = "uid";
    private static final String KEY_TUSERNAME = "username";
    private static final String KEY_TGAMBAR = "gambar";

    private  static  final String CREATE_TEMAN_TABLE = "CREATE TABLE " + TABLE_TEMAN + "("
            + KEY_TID + " INTEGER PRIMARY KEY," + KEY_TIDUSER + " TEXT,"
            + KEY_TUSERNAME + " TEXT," + KEY_TGAMBAR + " TEXT" + ")";

    private static final String TABLE_LOGIN = "login";

    // Login Table Columns names
    private static final String KEY_LID = "id";
    private static final String KEY_LIDUSER = "uid";

    private  static  final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
            + KEY_LID + " INTEGER PRIMARY KEY," + KEY_LIDUSER + " TEXT" + ")";

    private static final String TABLE_REGISTER = "register";

    // Login Table Columns names
    private static final String KEY_RID = "id";
    private static final String KEY_RIDUSER = "uid";

    private  static  final String CREATE_REGISTER_TABLE = "CREATE TABLE " + TABLE_REGISTER + "("
            + KEY_RID + " INTEGER PRIMARY KEY," + KEY_RIDUSER + " TEXT" + ")";



    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_TEMAN_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_REGISTER_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */

    //Add
    //Add User
    public void addUser(String iduser, String username, String password, String authkey, String level, String vemail, String vsms, String vbukti, String vupdate,
                        String ngroup, String phone, String alamat, String berdiri, String cabang, String gambar, String latitude, String longitude, String acara,
                        String menerima, String jangkawaktu, String timbalbalik, String sponsor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDUSER, iduser);
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_AUTHKEY, authkey);
        values.put(KEY_LEVEL, level);
        values.put(KEY_VEMAIL, vemail);
        values.put(KEY_VSMS, vsms);
        values.put(KEY_VBUKTI, vbukti);
        values.put(KEY_VUPDATE, vupdate);
        values.put(KEY_NGROUP, ngroup); // Email
        values.put(KEY_PHONE, phone); // Email
        values.put(KEY_ALAMAT, alamat); // Created At
        values.put(KEY_BERDIRI, berdiri); // Created At
        values.put(KEY_CABANG, cabang); // Created At
        values.put(KEY_GAMBAR, gambar); // Created At
        values.put(KEY_LATITUDE, latitude); // Created At
        values.put(KEY_LONGITUDE, longitude); // Created At
        values.put(KEY_ACARA, acara); // Created At
        values.put(KEY_MENERIMA, menerima); // Created At
        values.put(KEY_JANGKAWAKTU, jangkawaktu); // Created At
        values.put(KEY_TIMBAL_BALIK, timbalbalik); // Created At
        values.put(KEY_SPONSOR, sponsor); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    //Add Login
    public void addLogin(String uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LIDUSER, uid);

        // Inserting Row
        long id = db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    //Add Register
    public void addRegister(String uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RIDUSER, uid);

        // Inserting Row
        long id = db.insert(TABLE_REGISTER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */

    //Get
    //Get User Detail
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("username", cursor.getString(2));
            user.put("authkey", cursor.getString(4));
            user.put("level", cursor.getString(4));
            user.put("vemail", cursor.getString(6));
            user.put("vsms", cursor.getString(7));
            user.put("vbukti", cursor.getString(8));
            user.put("vupdate", cursor.getString(9));
            user.put("ngroup", cursor.getString(10));
            user.put("phone", cursor.getString(11));
            user.put("alamat", cursor.getString(12));
            user.put("berdiri", cursor.getString(13));
            user.put("cabang", cursor.getString(14));
            user.put("gambar", cursor.getString(15));
            user.put("latitude", cursor.getString(16));
            user.put("longitude", cursor.getString(17));
            user.put("acara", cursor.getString(18));
            user.put("menerima", cursor.getString(19));
            user.put("jangkawaktu", cursor.getString(20));
            user.put("timbalbalik", cursor.getString(21));
            user.put("sponsor", cursor.getString(22));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    //Get Register Index
    public HashMap<String, String> getRegister() {
        HashMap<String, String> regis = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_REGISTER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            regis.put("uid", cursor.getString(1));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + regis.toString());

        return regis;
    }

    public HashMap<String, String> getLogin() {
        HashMap<String, String> login = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            login.put("uid", cursor.getString(1));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + login.toString());

        return login;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void deleteRegister() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_REGISTER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void deleteLogin() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}