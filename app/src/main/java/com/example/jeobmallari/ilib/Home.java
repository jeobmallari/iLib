package com.example.jeobmallari.ilib;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String intentString = "query";
    GoogleApiClient mGoogleClient;
    SignedInGoogleClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        client = SignedInGoogleClient.getOurInstance();    // USE THIS TO REFER TO THE USER'S UP MAIL ACCT
        mGoogleClient = client.getmGoogleClient();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.logoutConfirmationBody)
                    .setTitle(R.string.logoutConfirmationTitle);
            builder.setPositiveButton(R.string.okOption, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    if(mGoogleClient.isConnected()) {
                        Auth.GoogleSignInApi.signOut(mGoogleClient);
                        Home.super.onBackPressed();
                    }
                    else{
                        Home.super.onBackPressed();
                    }
                }
            });
            builder.setNegativeButton(R.string.noOption, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_adv_srch) {
            // start adv search activity
            Intent intent = new Intent(this, AdvSearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            // start profile activity
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        } else if (id == R.id.nav_cart) {
            // start about activity
            Intent intent = new Intent(this, Cart.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            // start about activity
        } else if (id == R.id.nav_settings) {
            // start optional settings activity
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if(id == R.id.nav_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.logoutConfirmationBody)
                    .setTitle(R.string.logoutConfirmationTitle);
            builder.setPositiveButton(R.string.okOption, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    if(mGoogleClient.isConnected()) {
                        Auth.GoogleSignInApi.signOut(mGoogleClient);
                        mGoogleClient.disconnect();
                    }
                    Intent intent = new Intent(Home.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton(R.string.noOption, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    // do nothing
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void searchIsTapped(View view){
        // change to listview activity containing list of books
        Intent intent = new Intent(this, ResultsList.class);
        EditText et = (EditText) findViewById(R.id.et_query_home);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_home);
        String spinnerField = spinner.getSelectedItem().toString();
        String field = "";
        String[] spinnerArray = this.getResources().getStringArray(R.array.field);
        for(int i=0;i<spinnerArray.length;i++){
            if(spinnerField.equals(spinnerArray[i])){
                field = spinnerArray[i];
                break;
            }
        }
        String query1 = et.getText().toString();
        String query = query1+"~"+field;
        if(query.equals("")){
            Snackbar.make(view, "Enter search query", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
        else{
            if(field.equals("Any Field")) field = "title";

            String rawsql = "select title from books where " + field + " match '" + query1 + "';";
            DBHelper dbh = client.getDBHelper();
            SQLiteDatabase db = dbh.getReadableDatabase();
            Cursor c = db.rawQuery(rawsql, null);
            if(c.getCount() > 0) {
                query += "~basic";
                intent.putExtra(intentString, query);
                startActivity(intent);
            }
            else {
                Toast.makeText(this,"Query returned 0 results.",Toast.LENGTH_LONG).show();
            }
            c.close();
        }
    }
}
