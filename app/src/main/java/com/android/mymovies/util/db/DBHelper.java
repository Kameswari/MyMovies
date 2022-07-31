package com.android.mymovies.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.mymovies.MyApplication;

/**
 * DBHelper class for the application
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MyMovies.db";

    // all table names and column names are stored as constants to avoid typo errors
    public static final String TABLE_FAVORITES = "Favorites";
    public static final String IMDB_ID = "imdbID";
    public static final String TITLE = "title";
    public static final String YEAR = "year";
    public static final String TYPE = "type";
    public static final String POSTER = "poster";

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // singleton class
    // method to get the instance object with out context parameter
    public static synchronized DBHelper getInstance() {
        if (instance == null) {
            instance = new DBHelper(MyApplication.getInstance().getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table to store favorite movies
        sqLiteDatabase.execSQL("create table " + TABLE_FAVORITES + "(" + IMDB_ID + " text primary key not null unique, " + TITLE +
                " text, " + YEAR + " text, " + TYPE + " text, " + POSTER + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // No need to implement for now
    }
}
