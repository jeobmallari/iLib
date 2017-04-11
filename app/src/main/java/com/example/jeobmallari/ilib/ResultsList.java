package com.example.jeobmallari.ilib;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ResultsList extends AppCompatActivity implements RecycleViewAdapter.ListItemClickListener {

    private static int MAX_VAL;
    Toast mToast;
    String[] listItems;
    ArrayAdapter<String> adapter;
    RecycleViewAdapter rvAdapter;
    RecyclerView rv;
    public static String passed;
    public static String book;

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

        this.passed = this.getIntent().getStringExtra(Home.intentString);
        toolbar.setTitle(passed);

        listItems = getResources().getStringArray(R.array.sample_items);
        this.MAX_VAL = 100;

        rv = (RecyclerView) findViewById(R.id.rv_results_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rvAdapter = new RecycleViewAdapter(MAX_VAL, this.passed, this);
        rv.setAdapter(rvAdapter); //// TODO BY JEOB: ADD LINK FROM RV'S TEXTVIEW TO BOOKDETAIL ACTIVITY
    }

    public void onListItemClick(int clickedItemIndex, String bookTitle){
        if(mToast != null) mToast.cancel();

        String toastMessage = "Item #"+(clickedItemIndex+1)+", "+bookTitle+" clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        mToast.show();
        this.book = bookTitle;
        Intent intent = new Intent(this, BookDetail.class);
        intent.putExtra(Intent.EXTRA_TEXT, bookTitle);
        startActivity(intent);
    }

}
