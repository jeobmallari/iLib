package com.example.jeobmallari.ilib;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Jeob Mallari on 2/28/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance = null;
    private static final String DB_NAME = "miLibDB";
    private static final int DB_VERSION = 1;

    public static final String userTableName = "users";
    public static final String bookTableName = "books";
    public static final String reserveTableName = "reservations";
    public static final String borrowedTableName = "borrowed";

    public static final String col_title = "title";
    public static final String col_author = "author";
    public static final String col_accessionNo = "accessionNo";
    public static final String col_bookId = "bookID";
    public static final String col_callNo = "callNo";
    public static final String col_circType = "circulationType";
    public static final String col_format = "format";
    public static final String col_isbnNo = "isbnNo";
    public static final String col_location = "location";
    public static final String col_noOfCopies = "noOfCopies";
    public static final String col_publisher = "publisher";
    public static final String col_subject = "subject";
    public static final String col_volYear = "volYear";
    public static final String col_dateBorrowed = "dateBorrowed";
    public static final String col_dueDate = "dueDate";
    public static final String col_status = "status";

    public static final String user_col_id = "id";
    public static final String user_col_name = "name";
    public static final String user_col_displayname = "displayName";
    public static final String user_col_email = "email";
    public static final String user_col_displayPic = "displayPic";
    public static final String user_col_familyName = "familyName";
    public static final String user_col_givenName = "givenName";
    public static final String user_col_studno = "studentNo";
    public static final String user_col_homeadd = "homeadd";
    public static final String user_col_collegeadd = "collegeadd";
    public static final String user_col_bday = "bday";

    public static final String reserve_id = "resID";
    public static final String res_user_id = "userID";

    public static String jsonString = "";
    public static URL url;

    String dropDB = "DROP DATABASE IF EXISTS "+DB_NAME+";";
    String createDB = "CREATE DATABASE "+DB_NAME+";";
    String dropTable = "DROP TABLE IF EXISTS "+bookTableName+";";
    String dropUsers = "DROP TABLE IF EXISTS "+userTableName+";";
    String dropReservations = "DROP TABLE IF EXISTS "+reserveTableName+";";

    String createCommand = "CREATE VIRTUAL TABLE IF NOT EXISTS "+bookTableName+" USING fts4("
            +col_accessionNo+" TEXT NOT NULL, "
            +col_author+" TEXT NOT NULL, "
            +col_bookId+" INTEGER PRIMARY KEY NOT NULL, "
            +col_callNo+" TEXT NOT NULL, "
            +col_circType+" TEXT NOT NULL, "
            +col_dateBorrowed+" DATE, "
            +col_dueDate+" DATE, "
            +col_format+" TEXT NOT NULL, "
            +col_isbnNo+" TEXT NOT NULL, "
            +col_location+" TEXT NOT NULL, "
            +col_noOfCopies+" INTEGER NOT NULL, "
            +col_publisher+" TEXT NOT NULL, "
            +col_status+" varchar(10) not null, "
            +col_subject+" TEXT NOT NULL, "
            +col_title+" TEXT NOT NULL, "
            +col_volYear+" INTEGER NOT NULL);";

    String createUserTable = "CREATE VIRTUAL TABLE IF NOT EXISTS "+userTableName+" USING fts4("
            +user_col_id+" INTEGER PRIMARY KEY NOT NULL, "
            +user_col_name+" TEXT NOT NULL, "
            +user_col_displayname+" TEXT NOT NULL, "
            +user_col_familyName+" TEXT NOT NULL, "
            +user_col_givenName+" TEXT NOT NULL, "
            +user_col_displayPic+" TEXT NOT NULL, "
            +user_col_email+" TEXT NOT NULL, "
            +user_col_studno+" TEXT NOT NULL, "
            +user_col_homeadd+" TEXT NOT NULL, "
            +user_col_collegeadd+" TEXT NOT NULL, "
            +user_col_bday+" TEXT NOT NULL);";


    String createReservations = "CREATE VIRTUAL TABLE IF NOT EXISTS "+reserveTableName+" USING fts4("
            +reserve_id+" INTEGER PRIMARY KEY NOT NULL, "
            +col_bookId+" INTEGER NOT NULL, "
            +res_user_id+" INTEGER NOT NULL);";

    SQLiteDatabase myWritableDB;
    SQLiteDatabase myReadableDB;
    Context context;

    public static synchronized DBHelper getInstance(Context c){
        if(sInstance == null){
            sInstance = new DBHelper(c.getApplicationContext());
        }
        return sInstance;
    }

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        Log.e("DB", "Database Initialized@"+context.toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createCommand);
        db.execSQL(createUserTable);
        db.execSQL(createReservations);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(dropTable);
        sqLiteDatabase.execSQL(dropUsers);
        sqLiteDatabase.execSQL(dropReservations);
        onCreate(sqLiteDatabase);
    }
}
