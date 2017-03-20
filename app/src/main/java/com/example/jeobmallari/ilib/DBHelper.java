package com.example.jeobmallari.ilib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jeob Mallari on 2/28/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "miLibDB.db";
    private static final int DB_VERSION = 1;

    public static final String userTableName = "User";
    public static final String bookTableName = "Book";
    public static final String book_col_title = "title";
    public static final String book_col_author = "author";

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
