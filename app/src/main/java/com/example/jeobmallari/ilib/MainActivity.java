package com.example.jeobmallari.ilib;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private String[] listEntries;
    private DrawerLayout dl;
    private ListView drawerList;
    private Button loginButton;
    private Button signupButton;

    static String intentMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listEntries = getResources().getStringArray(R.array.list_array);
        dl = (DrawerLayout) findViewById(R.id.activity_main);
        drawerList = (ListView) findViewById(R.id.left_drawer);

    }

    public void setSignup(View view){

    }

    public void verify(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.unameField);
        String message = "Hello " + editText.getText().toString() + "!";
        intent.putExtra(this.intentMsg, message);
        startActivity(intent);
    }
}