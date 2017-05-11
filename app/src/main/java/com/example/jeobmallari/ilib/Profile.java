package com.example.jeobmallari.ilib;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import static com.example.jeobmallari.ilib.DBHelper.col_bookId;
import static com.example.jeobmallari.ilib.DBHelper.res_user_id;
import static com.example.jeobmallari.ilib.DBHelper.reserveTableName;
import static com.example.jeobmallari.ilib.DBHelper.reserve_id;

public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecycleViewAdapter.ListItemClickListener {

    static ImageView iv_pic;
    static RecyclerView rv;
    static RecycleViewAdapter rvAdapter;
    static GoogleApiClient mGoogleClient;
    static SignedInGoogleClient client;
    Toast mToast;

    String book;
    static ArrayList<String> mats;

    public interface ResultCallback{
        public void onRespond();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        client = SignedInGoogleClient.getOurInstance();
        TextView tv_name = (TextView) findViewById(R.id.profileName);
        iv_pic = (ImageView) findViewById(R.id.imageView6);
        tv_name.setText(client.getGivenName()+" "+client.getFamilyName());
        mGoogleClient = client.getmGoogleClient();
        mats = new ArrayList<String>();

        ResultCallback rcb = new ResultCallback() {
            @Override
            public void onRespond() {
                rv = (RecyclerView) findViewById(R.id.rv_profile);
                LinearLayoutManager layoutManager = new LinearLayoutManager(Profile.this);
                rv.setLayoutManager(layoutManager);
                rv.setHasFixedSize(true);
                // set arraylist to pass to rvAdapter
                rvAdapter = new RecycleViewAdapter(mats, Profile.this);
                rv.setAdapter(rvAdapter);
                Log.e("Result Callback: ", "Adapter bound to RecyclerView. Length = " + mats.size());
            }
        };

        new CartCheckerTask(rcb).execute(getResources().getString(R.string.reservations_json_query));

        if(client.getDisplayPic() != null){
            URL url = null;
            try {
                Log.e("SEE THIS: ", client.getName());
                url = new URL(client.getDisplayPic().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            new DownloadImageTask(Profile.iv_pic).execute(url.toString());
            Log.e("Image Bitmap", client.getDisplayPic().toString());
        }
        else {
            Toast.makeText(Profile.this, "Please set a profile picture in your Google Account.", Toast.LENGTH_LONG).show();
        }

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
        getMenuInflater().inflate(R.menu.profile, menu);
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

        if (id == R.id.nav_adv_srch) {
            // start adv search activity
            Intent intent = new Intent(this, AdvSearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            // start profile activity
            Intent intent = new Intent(this, Profile.class);
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
                    Intent intent = new Intent(Profile.this, LoginActivity.class);
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

    public void onListItemClick(int clickedItemIndex, String bookTitle){
        if(mToast != null) mToast.cancel();
        this.book = bookTitle;
        Intent intent = new Intent(this, BookDetail.class);
        intent.putExtra(Intent.EXTRA_TEXT, bookTitle);
        startActivity(intent);

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        ProgressDialog progressDialog;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Profile.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Loading Image...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            progressDialog.dismiss();
            Log.e("Image Loaded", SignedInGoogleClient.getOurInstance().getDisplayPic().toString());
        }
    }

    private class CartCheckerTask extends AsyncTask<String, Void, String>{
        String reservations;
        ProgressDialog progressDialog;
        int toReturn=0;
        ResultCallback rcb;

        public CartCheckerTask(ResultCallback callback){
            this.rcb = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Profile.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Loading Cart...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String link = strings[0];
            try{
                InputStream in = new java.net.URL(link).openStream();
                Scanner scanner = new Scanner(in).useDelimiter("\\A");
                reservations = scanner.hasNext() ? scanner.next() : "";
                //Log.e("JSON: Reservations: ", reservations);

                JSONObject reserves = new JSONObject(reservations);
                Iterator<String> iterator = reserves.keys();
                ArrayList<String> keys = new ArrayList<String>();
                if(iterator.hasNext()){
                    do{
                        keys.add(iterator.next());
                    }while(iterator.hasNext());
                }

                for(int a=0;a<reserves.length();a++){
                    JSONObject res = reserves.getJSONObject(keys.get(a));
                    Log.e("JSON: Res: ", res.toString());
                    if(res.getString(res_user_id).equals(client.getId())){
                        // ------------------------------------------------
                        toReturn++;
                        String bookTitle = res.getString("book");
                        String author = res.getString("author");
                        String toPass = bookTitle+","+author;
                        Profile.mats.add(toPass);

                        Log.e("doInBackground: ", Profile.mats.get(a));
                        // ------------------------------------------------
                    }
                }
                in.close();
                scanner.close();
            }catch(JSONException e){
                Log.e("JSON err: Profile", "Reparse json array");
            } catch(IOException ioe){
                ioe.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.e("Cart Loader: ", "onPostExecute");
            rcb.onRespond();
        }
    }

}
