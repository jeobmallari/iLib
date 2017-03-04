package com.example.jeobmallari.ilib;

import android.widget.TextView;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Jeob Mallari on 3/4/2017.
 */

public class DisplayMessageActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_message_activity);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.intentMsg);

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.tv_display_message);
        textView.setText(message);
    }


}
