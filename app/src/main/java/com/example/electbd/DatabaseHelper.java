package com.example.electbd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database constants
    private static final String DATABASE_NAME = "NationalServer";
    private static final int DATABASE_VERSION = 3;

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_CANDIDATES = "candidates";

    // Column names for users table
    private static final String COL_USERNAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_NID = "nid";
    private static final String COL_BIRTHDAY = "birthday";
    private static final String COL_MOBILE = "mobile";
    private static final String COL_PASSWORD = "password";

    // Column names for candidates table
    static final String COL_ID = "id";
    static final String COL_NAME = "name";
    static final String COL_VOTES = "votes";
    static final String COL_PRODUCT_IMAGE_URI = "product_image_uri";


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

        // Creating the candidates table
        String createCandidatesTable = "CREATE TABLE " + TABLE_CANDIDATES + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_VOTES + " INTEGER DEFAULT 0, " +
                COL_PRODUCT_IMAGE_URI + " BLOB)";

        db.execSQL(createCandidatesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CANDIDATES);
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

        return false;
    }
}
