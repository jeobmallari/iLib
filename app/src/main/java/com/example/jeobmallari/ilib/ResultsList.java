package com.example.jeobmallari.ilib;

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

import java.util.ArrayList;

public class ResultsList extends AppCompatActivity {

    private static int MAX_VAL;

    String[] listItems;
    ArrayAdapter<String> adapter;
    RecycleViewAdapter rvAdapter;
    RecyclerView rv;
    public static String passed;
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
        rvAdapter = new RecycleViewAdapter(MAX_VAL, this.passed);
        rv.setAdapter(rvAdapter);
    }

    public void populate(){
        int arrLen = MAX_VAL;
        for(int i=0;i<arrLen;i++) {
            String text = "Book #" + (arrLen + i + 1) + " about " + passed;
            listItems[arrLen+i] = text;
        }
    }
}
