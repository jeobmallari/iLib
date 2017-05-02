package com.example.jeobmallari.ilib;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;

public class AdvSearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    GoogleApiClient mGoogleClient;
    SignedInGoogleClient client;

    Spinner field1;
    Spinner field2;
    Spinner field3;

    EditText expr1;
    EditText expr2;
    EditText expr3;

    Spinner op1;
    Spinner op2;

    Spinner spinner_location;
    Spinner spinner_type;
    Spinner spinner_format;

    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Advanced Search");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        client = SignedInGoogleClient.getOurInstance();
        mGoogleClient = client.getmGoogleClient();

        field1 = (Spinner) findViewById(R.id.spinner);
        field2 = (Spinner) findViewById(R.id.spinner5);
        field3 = (Spinner) findViewById(R.id.spinner3);

        expr1 = (EditText) findViewById(R.id.editText);
        expr2 = (EditText) findViewById(R.id.editText2);
        expr3 = (EditText) findViewById(R.id.editText3);

        op1 = (Spinner) findViewById(R.id.spinner2);
        op2 = (Spinner) findViewById(R.id.spinner4);

        spinner_location = (Spinner) findViewById(R.id.spinner7);
        spinner_type = (Spinner) findViewById(R.id.spinner8);
        spinner_format = (Spinner) findViewById(R.id.spinner9);

        searchButton = (Button) findViewById(R.id.button);
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart, menu);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // start adv search activity
            Intent intent = new Intent(this, Home.class);
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
                    Intent intent = new Intent(AdvSearchActivity.this, LoginActivity.class);
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

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.button){
            startAdvSearch();
        }
    }

    public void startAdvSearch(){
        // TODO put "~adv" at the end of the intent string to let ResultsList know that this is advanced search
        String default_field_value = "Any Field";
        String default_location_value = "Any Location";
        String default_type_value = "Any Type";
        String default_format_value = "Any Format";
        DBHelper dbHelper = client.getDBHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;

        String rawSQL = "select " + dbHelper.col_title
                + " from " +dbHelper.bookTableName
                + " where ";


        // FULLTEXT SEARCH USING "MATCH" ON FIELD AND EXPRESSION
        if(!field1.getSelectedItem().toString().equals(default_field_value) && !(expr1.getText().toString().equals(""))){
            rawSQL += field1.getSelectedItem().toString()
                    + " like '%" + expr1.getText().toString() + "%'";
        } else {
            rawSQL += dbHelper.col_title
                + " like '%" + expr1.getText().toString() + "%'";
        }

        if(!field2.getSelectedItem().toString().equals(default_field_value) && !(expr2.getText().toString().equals(""))){
            rawSQL += " " + op1.getSelectedItem().toString() + " "
                    + field2.getSelectedItem().toString()
                    + " like '%" + expr2.getText().toString() + "%'";
        } else {
            if(field2.getSelectedItem().toString().equals(default_field_value) && !(expr2.getText().toString().equals(""))){
                rawSQL += " " + op1.getSelectedItem().toString() + " "
                        + dbHelper.col_title
                        + " like '%" + expr2.getText().toString() + "%'";
            }
        }

        // may field value, may laman
        if(!field3.getSelectedItem().toString().equals(default_field_value) && !(expr3.getText().toString().equals(""))){
            rawSQL += " " + op2.getSelectedItem().toString() + " "
                    + field3.getSelectedItem().toString()
                    + " like '%" + expr3.getText().toString() + "%'";
        } else {  // default field value, at may laman
            if(field3.getSelectedItem().toString().equals(default_field_value) && !(expr3.getText().toString().equals(""))){
                rawSQL += " " + op2.getSelectedItem().toString() + " "
                        + dbHelper.col_title
                        + " like '%" + expr3.getText().toString() + "%'";
            }
        }

        // FULLTEXT SEARCH USING "MATCH" ON LOCATION, TYPE AND FORMAT
        if(!spinner_location.getSelectedItem().toString().equals(default_location_value)){
            rawSQL += " OR " + dbHelper.col_location
                    + " like '%" + spinner_location.getSelectedItem().toString() + "%'";
        }

        if(!spinner_type.getSelectedItem().toString().equals(default_type_value)){
            rawSQL += " OR " + dbHelper.col_circType
                    + " like '%" +spinner_type.getSelectedItem().toString() + "%'";
        }

        if(!spinner_format.getSelectedItem().toString().equals(default_format_value)){
            rawSQL += " OR " + dbHelper.col_format
                    + " like '%" + spinner_format.getSelectedItem().toString() + "%'";
        }
        Log.e("ADVSRCH QRY: ", rawSQL);
        cursor = db.rawQuery(rawSQL, null);
        if(cursor.getCount() > 0){
            rawSQL += "~search~adv";   // accomplish to-do
            Log.e("query successful: ", cursor.getCount() + " returned from "+rawSQL);
            Intent intent = new Intent(this, ResultsList.class);
            intent.putExtra("query", rawSQL);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this,"Query returned 0 results.",Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }
}
