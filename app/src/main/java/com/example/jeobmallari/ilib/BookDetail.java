package com.example.jeobmallari.ilib;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class BookDetail extends AppCompatActivity {

    SQLiteDatabase db;
    DBHelper dbHelper;
    Cursor cursor;
    String bookTitle;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_book_layout);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveMaterial();
            }
        });

        bookTitle = this.getIntent().getStringExtra(Intent.EXTRA_TEXT);

        getSupportActionBar().setTitle(bookTitle);

        setElements();

    }

    public void reserveMaterial(){
        // see number of copies
        // check if reservations are full
        // else add json values to firebase rtdb respectively
    }

    public void setElements(){
        dbHelper = LoginActivity.dbHelper;
        db = dbHelper.getReadableDatabase();
        String q = "select * from "+dbHelper.bookTableName
                +" where "+dbHelper.col_title
                +" = '"+this.bookTitle+"'";
        cursor = db.rawQuery(q, null);
        Log.e("BookDetail Q: ", q);
        if(cursor.moveToFirst()){
            do{
                TextView tv1 = (TextView) findViewById(R.id.tv_bookID_toChange);
                tv1.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_bookId)));
                TextView tv2 = (TextView) findViewById(R.id.tv_title_toChange);
                tv2.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_title)));
                TextView tv3 = (TextView) findViewById(R.id.tv_author_toChange);
                tv3.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_author)));
                TextView tv4 = (TextView) findViewById(R.id.tv_publisher_toChange);
                tv4.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_publisher)));
                TextView tv5 = (TextView) findViewById(R.id.tv_subject_toChange);
                tv5.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_subject)));
                TextView tv6 = (TextView) findViewById(R.id.tv_accNo_toChange);
                tv6.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_accessionNo)));
                TextView tv7 = (TextView) findViewById(R.id.tv_isbn_toChange);
                tv7.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_isbnNo)));
                TextView tv8 = (TextView) findViewById(R.id.tv_callNo_toChange);
                tv8.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_callNo)));
                TextView tv9 = (TextView) findViewById(R.id.tv_format_toChange);
                tv9.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_format)));
                TextView tv10 = (TextView) findViewById(R.id.tv_location_toChange);
                tv10.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_location)));
                TextView tv11 = (TextView) findViewById(R.id.tv_status_toChange);
                tv11.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_status)));
                TextView tv12 = (TextView) findViewById(R.id.tv_type_toChange);
                tv12.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_circType)));
                TextView tv13 = (TextView) findViewById(R.id.tv_volyear_toChange);
                tv13.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_volYear)));
                TextView tv14 = (TextView) findViewById(R.id.tv_copies_toChange);
                tv14.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_noOfCopies)));
            }while(cursor.moveToNext());
        }
    }
}
