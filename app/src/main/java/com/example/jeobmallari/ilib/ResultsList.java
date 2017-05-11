package com.example.jeobmallari.ilib;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static com.example.jeobmallari.ilib.DBHelper.col_accessionNo;
import static com.example.jeobmallari.ilib.DBHelper.col_author;
import static com.example.jeobmallari.ilib.DBHelper.col_bookId;
import static com.example.jeobmallari.ilib.DBHelper.col_callNo;
import static com.example.jeobmallari.ilib.DBHelper.col_circType;
import static com.example.jeobmallari.ilib.DBHelper.col_dateBorrowed;
import static com.example.jeobmallari.ilib.DBHelper.col_dueDate;
import static com.example.jeobmallari.ilib.DBHelper.col_format;
import static com.example.jeobmallari.ilib.DBHelper.col_isbnNo;
import static com.example.jeobmallari.ilib.DBHelper.col_location;
import static com.example.jeobmallari.ilib.DBHelper.col_noOfCopies;
import static com.example.jeobmallari.ilib.DBHelper.col_publisher;
import static com.example.jeobmallari.ilib.DBHelper.col_status;
import static com.example.jeobmallari.ilib.DBHelper.col_subject;
import static com.example.jeobmallari.ilib.DBHelper.col_title;
import static com.example.jeobmallari.ilib.DBHelper.col_volYear;

public class ResultsList extends AppCompatActivity implements RecycleViewAdapter.ListItemClickListener {

    Toast mToast;
    RecycleViewAdapter rvAdapter;
    RecyclerView rv;
    public static String passed;
    public static String book;
    public static Context context;
    SignedInGoogleClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        context = ResultsList.this;

        this.passed = this.getIntent().getStringExtra(Home.intentString);
        toolbar.setTitle(passed);

        rv = (RecyclerView) findViewById(R.id.rv_results_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        ArrayList<String> items = new ArrayList<String>();

        String delimiter = "~";
        String[] tokens = this.passed.split(delimiter);
        Log.e("Passed: ", this.passed);
        Log.e("Token[0]: ", tokens[0]);
        Log.e("Tokens[1]: ", tokens[1]);
        Log.e("Tokens[2]: ", tokens[2]);
        client = SignedInGoogleClient.getOurInstance();
        DBHelper dbHelper = client.getDBHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        if(tokens[2].equals("basic")) {
            // -------------------------------------------
            // BASIC SEARCH STARTED FROM HOME ACTIVITY
            // -------------------------------------------
            if (tokens[1].equals("Any Field")) {
                tokens[1] = dbHelper.col_title;
            }

            String rawQry = "SELECT " + dbHelper.col_title
                    + ", " + dbHelper.col_author
                    + " FROM " + dbHelper.bookTableName
                    + " WHERE " + tokens[1]
                    + " MATCH '" + tokens[0] + "';";
            try {
                Log.e("Raw Query: ", rawQry);
                Cursor cursor = db.rawQuery(rawQry, null);
                //Toast.makeText(this, "Cursor size: " + cursor.getCount(), Toast.LENGTH_LONG).show();
                if (cursor.moveToFirst()) {
                    do {
                        String takenTitle = cursor.getString(cursor.getColumnIndex(dbHelper.col_title));
                        String author = cursor.getString(cursor.getColumnIndex(dbHelper.col_author));
                        Log.e("Item to add: ", takenTitle);
                        items.add(takenTitle+","+author);
                    } while (cursor.moveToNext());
                } else {
                    Toast.makeText(this, "Error in executing raw query", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            } catch (SQLiteException e) {
                Log.e("SEARCH ERR", "Invalid SQL");
                e.printStackTrace();
            }
        }
        else if(tokens[2].equals("adv")){
            // ------------------------------------------------------
            // ADVANCED SEARCH STARTED FROM ADVSEAERCHACTIVITY
            // ------------------------------------------------------
            // tokens[1] = search; tokens[2] = adv // nothing to do with them
            // tokens[0] = full query;

            String rawQry = tokens[0];

            try {
                Log.e("Raw Query: ", rawQry);
                Cursor cursor = db.rawQuery(rawQry, null);
                if (cursor.moveToFirst()) {
                    do {
                        String takenTitle = cursor.getString(cursor.getColumnIndex(dbHelper.col_title));
                        String author = cursor.getString(cursor.getColumnIndex(dbHelper.col_author));
                        Log.e("Item to add: ", takenTitle);
                        items.add(takenTitle+","+author);
                    } while (cursor.moveToNext());
                } else {
                    Toast.makeText(this, "Error in executing raw query", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            } catch (SQLiteException e) {
                Log.e("SEARCH ERR", "Invalid SQL");
                e.printStackTrace();
            }

        }

        rvAdapter = new RecycleViewAdapter(items, this);
        rv.setAdapter(rvAdapter);
    }

    public void onListItemClick(int clickedItemIndex, String bookTitle){
        if(mToast != null) mToast.cancel();
        this.book = bookTitle;
        Intent intent = new Intent(this, BookDetail.class);
        intent.putExtra(Intent.EXTRA_TEXT, bookTitle);
        startActivity(intent);

    }

}