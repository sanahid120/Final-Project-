package com.example.electbd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database constants
    private static final String DATABASE_NAME = "NationalServer";
    private static final int DATABASE_VERSION = 7;

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_CANDIDATES = "candidates";
    private static final String TABLE_ID_SERVER = "id_server";
    private static final String TABLE_ADMIN_INFO = "admin_info";

    // Column names for ID server table
    private static final String COL_ID_SERVER_ID = "server_id";
    private static final String COL_ID_SERVER_NAME = "name";
    private static final String COL_ID_SERVER_F_NAME = "father_name";
    private static final String COL_ID_SERVER_M_NAME = "mother_name";
    private static final String COL_ID_SERVER_PHONE = "phone";
    private static final String COL_ID_SERVER_ADDRESS = "address";
    private static final String COL_ID_SERVER_IMAGE = "image";



    // Column names for users table
    static final String COL_USERNAME = "username";
    static final String COL_EMAIL = "email";
    static final String COL_NID = "nid";
    static final String COL_BIRTHDAY = "birthday";
    static final String COL_MOBILE = "mobile";
    static final String COL_PASSWORD = "password";

    // Column names for candidates table
    static final String COL_ID = "id";
    static final String COL_NAME = "name";
    static final String COL_VOTES = "votes";
    static final String COL_PRODUCT_IMAGE_URI = "product_image_uri";

    static final String COL_ADMIN_USERNAME= "admin_username";
    static final String COL_ADMIN_PASSWORD= "admin_password";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating the users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USERNAME + " TEXT NOT NULL, " +
                COL_EMAIL + " TEXT, " +
                COL_NID + " TEXT PRIMARY KEY, " + // Assuming NID is unique
                COL_BIRTHDAY + " TEXT NOT NULL, " +
                COL_MOBILE + " TEXT NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL)";

        db.execSQL(createUsersTable);


        String createAdminTable = "CREATE TABLE " + TABLE_ADMIN_INFO + " (" +
                COL_ADMIN_USERNAME + " TEXT NOT NULL, " +
                COL_ADMIN_PASSWORD + " TEXT NOT NULL)";

        db.execSQL(createAdminTable);

        // Creating the candidates table
        String createCandidatesTable = "CREATE TABLE " + TABLE_CANDIDATES + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_VOTES + " INTEGER DEFAULT 0, " +
                COL_PRODUCT_IMAGE_URI + " BLOB)";

        db.execSQL(createCandidatesTable);

        String createIDServerTable = "CREATE TABLE " + TABLE_ID_SERVER + " (" +
                COL_ID_SERVER_ID + " TEXT PRIMARY KEY, " +
                COL_ID_SERVER_NAME + " TEXT NOT NULL, " +
                COL_ID_SERVER_F_NAME + " TEXT, " +
                COL_ID_SERVER_M_NAME + " TEXT, " +
                COL_ID_SERVER_PHONE + " TEXT NOT NULL, " +
                COL_ID_SERVER_ADDRESS + " TEXT, " +
                COL_ID_SERVER_IMAGE + " BLOB)";
        db.execSQL(createIDServerTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CANDIDATES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ID_SERVER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN_INFO);
        // Create tables again
        onCreate(db);
    }

    public boolean insertUser(String username, String email, String nid, String birthday, String mobile, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_NID, nid);
        contentValues.put(COL_BIRTHDAY, birthday);
        contentValues.put(COL_MOBILE, mobile);
        contentValues.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, contentValues);
        db.close();

        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS +
                        " WHERE (" + COL_EMAIL + " = ? OR " + COL_MOBILE + " = ?) AND " + COL_PASSWORD + " = ?",
                new String[]{username, username, password}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void insertCandidate(String id, String name, byte[] imageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_ID, id);
        values.put(COL_PRODUCT_IMAGE_URI, imageByteArray);

        db.insert(TABLE_CANDIDATES, null, values);
        db.close();
    }

    public Cursor getCandidates() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id AS _id, name, votes, product_image_uri FROM candidates";
        return db.rawQuery(query, null);
    }

    public Cursor getByCid(String cid) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CANDIDATES + " WHERE " + COL_ID + " = ?", new String[]{cid});
    }

    public boolean updateCandidate(String candidateId, String candidateName, byte[] imageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_NAME, candidateName);
        contentValues.put(DatabaseHelper.COL_PRODUCT_IMAGE_URI, imageByteArray);

        int rowsAffected = db.update(
                DatabaseHelper.TABLE_CANDIDATES,
                contentValues,
                DatabaseHelper.COL_ID + " = ?",
                new String[]{candidateId}
        );
        db.close();

        return rowsAffected > 0;
    }


    public boolean deleteCandidate(String candidateID) {

        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(
                DatabaseHelper.TABLE_CANDIDATES,
                DatabaseHelper.COL_ID + " = ?",
                new String[]{candidateID}
        );


        db.close();


        return rowsDeleted > 0;

    }
    public boolean incrementVoteCount(int candidateID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_VOTES + " FROM " + TABLE_CANDIDATES + " WHERE " + COL_ID + " = ?", new String[]{String.valueOf(candidateID)});
        if (cursor != null && cursor.moveToFirst()) {
            int currentVotes = cursor.getInt(cursor.getColumnIndexOrThrow(COL_VOTES));
            ContentValues values = new ContentValues();
            values.put(COL_VOTES, currentVotes + 1);

            int rowsAffected = db.update(TABLE_CANDIDATES, values, COL_ID + " = ?", new String[]{String.valueOf(candidateID)});
            cursor.close();
            db.close();
            return rowsAffected > 0;
        }
        db.close();
        return false;
    }

    public boolean InsertIDServer(String insertId, String insertName, String insertFName, String insertMName, String insertPhone, String insertAddress, byte[] imageByteArray) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ID_SERVER_ID, insertId);
        values.put(COL_ID_SERVER_NAME, insertName);
        values.put(COL_ID_SERVER_F_NAME, insertFName);
        values.put(COL_ID_SERVER_M_NAME, insertMName);
        values.put(COL_ID_SERVER_PHONE, insertPhone);
        values.put(COL_ID_SERVER_ADDRESS, insertAddress);
        values.put(COL_ID_SERVER_IMAGE, imageByteArray);

        long result = db.insert(TABLE_ID_SERVER, null, values);
        db.close();
        return result != -1;
    }



    public Cursor getUserInfo(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Use OR to check for email or phone
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ? OR " + COL_MOBILE + " = ?";
        return db.rawQuery(query, new String[]{username, username});
    }


    public void updateUsername(String userId, String newUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME, newUsername);
        db.update(TABLE_USERS, contentValues, COL_NID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }

    public boolean isAdminExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ADMIN_INFO, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Insert default admin credentials if they do not exist
    public boolean insertAdminCredentials(String username, String password) {
        if (!isAdminExists()) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_ADMIN_USERNAME, username);
            contentValues.put(COL_ADMIN_PASSWORD, password);
            long result = db.insert(TABLE_ADMIN_INFO, null, contentValues);
            db.close();
            return result != -1;
        }
        return false; // Admin already exists
    }

    // Check if given credentials match the admin credentials in the database
    public boolean checkAdminCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ADMIN_INFO +
                        " WHERE " + COL_ADMIN_USERNAME + " = ? AND " + COL_ADMIN_PASSWORD + " = ?",
                new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }
    public String getCurrentAdminUsername() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_ADMIN_USERNAME + " FROM " + TABLE_ADMIN_INFO, null);
        String username = null;
        if (cursor != null && cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndexOrThrow(COL_ADMIN_USERNAME));
            cursor.close();
        }
        return username;
    }

    public String getCurrentAdminPassword() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_ADMIN_PASSWORD + " FROM " + TABLE_ADMIN_INFO, null);
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndexOrThrow(COL_ADMIN_PASSWORD));
            cursor.close();
        }
        return password;
    }


    // Update admin username and password
    public boolean updateAdminCredentials(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ADMIN_USERNAME, username);
        contentValues.put(COL_ADMIN_PASSWORD, newPassword);

        int rowsAffected = db.update(TABLE_ADMIN_INFO, contentValues, null, null);
        db.close();
        return rowsAffected > 0;
    }

/*
    public boolean updateIDServer(String id, String name, String fatherName, String motherName, String phone, String address, byte[] imageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ID_SERVER_NAME, name);
        values.put(COL_ID_SERVER_F_NAME, fatherName);
        values.put(COL_ID_SERVER_M_NAME, motherName);
        values.put(COL_ID_SERVER_PHONE, phone);
        values.put(COL_ID_SERVER_ADDRESS, address);
        values.put(COL_ID_SERVER_IMAGE, imageByteArray);

        int rowsAffected = db.update(TABLE_ID_SERVER, values, COL_ID_SERVER_ID + " = ?", new String[]{id});
        db.close();
        return rowsAffected > 0;
    }
*/

    /*
    public boolean deleteIDServer(String id) {
    SQLiteDatabase db = this.getWritableDatabase();
    int rowsDeleted = db.delete(TABLE_ID_SERVER, COL_ID_SERVER_ID + " = ?", new String[]{id});
    db.close();
    return rowsDeleted > 0;
    }
    */
}
