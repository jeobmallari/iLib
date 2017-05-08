package com.example.jeobmallari.ilib;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Scanner;

public class BookDetail extends AppCompatActivity {

    SQLiteDatabase db;
    DBHelper dbHelper;
    Cursor cursor;
    static String bookTitle;
    String query;

    static Context context;

    static TextView tv1;
    static TextView tv2;
    static TextView tv3;
    static TextView tv4;
    static TextView tv5;
    static TextView tv6;
    static TextView tv7;
    static TextView tv8;
    static TextView tv9;
    static TextView tv10;
    static TextView tv11;
    static TextView tv12;
    static TextView tv13;
    static TextView tv14;

    static SignedInGoogleClient client;

    static boolean isAlreadyReserved = false;
    static int takenCopies = 0;
    static int numberOfReservations = 0;
    static String bookId;
    static int numberOfCopies;

    ScheduleClient scheduleClient;

    public interface TheCallback{
        public void onCallback();
    }

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
                AlertDialog.Builder builder = new AlertDialog.Builder(BookDetail.this);
                builder.setMessage(R.string.askToReserve_body)
                        .setTitle(R.string.askToReserve_title);
                builder.setPositiveButton(R.string.okOption, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        // set flag in BookDetail to call material reservation function
                        // ---------------------------------
                        // call asynctask here
                        TheCallback tcb = new TheCallback() {
                            @Override
                            public void onCallback() {
                                reserveMaterial();
                            }
                        };
                        CounterTask ct = new CounterTask(tcb);
                        String url = "https://fir-milib.firebaseio.com/reservations/.json?print=pretty";
                        ct.execute(url);
                        // ---------------------------------
                        // reserveMaterial();
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
        });

        bookTitle = this.getIntent().getStringExtra(Intent.EXTRA_TEXT);
        context = this;
        client = SignedInGoogleClient.getOurInstance();
        getSupportActionBar().setTitle(bookTitle);
        setElements();

        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();
    }

    public static void showMaterialUnavailable(){
        AlertDialog.Builder builder = new AlertDialog.Builder(BookDetail.context);
        builder.setMessage(R.string.materialUnavailable_body)
        .setTitle(R.string.materialUnavailable_title);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void alreadyReserved(){
        AlertDialog.Builder builder = new AlertDialog.Builder(BookDetail.context);
        builder.setMessage(R.string.alreadyReserved_body)
                .setTitle(R.string.alreadyReserved_title);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void reserveMaterial(){
        final DatabaseReference reservations = FirebaseDatabase.getInstance().getReference().child("reservations");

        ArrayList<String> keys = new ArrayList<String>();

        reservations.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    if(snap.child("bookId").equals(bookId)) takenCopies++;
                    if(snap.child("userID").getValue().equals(client.getId()) && snap.child("bookId").getValue().equals(bookId)){
                        isAlreadyReserved = true;   // if user has already reserved the item
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(isAlreadyReserved) {
            alreadyReserved();
        }
        else if(takenCopies == numberOfCopies){
            // material is no longer available
            BookDetail.showMaterialUnavailable();
        }
        else{
            // material is available
            // add reservation to reservations table
            Reservation reservation = new Reservation();
            reservation.setBookId(BookDetail.bookTitle);
            reservation.setResID((numberOfReservations+1)+"");
            reservation.setUserID(client.getDisplayName());
            String key = reservations.push().getKey();
            reservations.child(key).setValue(reservation);

            Log.e("Reservations: ", "reserve successful");

            AlertDialog.Builder builder = new AlertDialog.Builder(BookDetail.context);
            builder.setTitle(R.string.reservationSuccess_title)
                    .setMessage(R.string.reservationSuccess_body);
            AlertDialog dialog = builder.create();
            dialog.show();
            // --------------------------------------------------------
            // startAlarm();
            scheduleJob();
            // --------------------------------------------------------
        }
    }

    public void scheduleJob(){
        NotificationUtilities.fireNotification(this);
    }

    public void startAlarm(){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minutes = Calendar.getInstance().get(Calendar.MINUTE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
//        if((hour+21) > 24){       // for 21-hour interval
//            hour = (hour+1) % 24;
//            calendar.set(Calendar.DAY_OF_MONTH, ++day);
//            calendar.set(Calendar.HOUR_OF_DAY, hour);
//        }
//        else{
//            hour += 21;
//            calendar.set(Calendar.DAY_OF_MONTH, day);
//            calendar.set(Calendar.HOUR_OF_DAY, hour);
//        }

//        if((hour+1) > 24){       // for 1-hour interval
//            hour = (hour+1) % 24;
//            calendar.set(Calendar.DAY_OF_MONTH, ++day);
//            calendar.set(Calendar.HOUR_OF_DAY, hour);
//        }
//        else{
//            hour += 1;
//            calendar.set(Calendar.DAY_OF_MONTH, day);
//            calendar.set(Calendar.HOUR_OF_DAY, hour);
//        }

        if((minutes+30) > 60){      // for 10-minute interval
            minutes = (minutes+10) % 60;
            calendar.set(Calendar.HOUR_OF_DAY, ++hour);
            calendar.set(Calendar.MINUTE, minutes);
        }
        else{
            minutes += 30;
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minutes);
        }
        scheduleClient.setAlarmForNotification(calendar);
        String msg = "Notification set for: "+ (month+1) +"/"+ day +"/"+ year + ", @ "+hour+ ":"+minutes;
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        Log.e("Notifications: ", msg);
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
                tv1 = (TextView) findViewById(R.id.tv_bookID_toChange);
                tv1.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_bookId)));
                tv2 = (TextView) findViewById(R.id.tv_title_toChange);
                tv2.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_title)));
                tv3 = (TextView) findViewById(R.id.tv_author_toChange);
                tv3.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_author)));
                tv4 = (TextView) findViewById(R.id.tv_publisher_toChange);
                tv4.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_publisher)));
                tv5 = (TextView) findViewById(R.id.tv_subject_toChange);
                tv5.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_subject)));
                tv6 = (TextView) findViewById(R.id.tv_accNo_toChange);
                tv6.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_accessionNo)));
                tv7 = (TextView) findViewById(R.id.tv_isbn_toChange);
                tv7.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_isbnNo)));
                tv8 = (TextView) findViewById(R.id.tv_callNo_toChange);
                tv8.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_callNo)));
                tv9 = (TextView) findViewById(R.id.tv_format_toChange);
                tv9.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_format)));
                tv10 = (TextView) findViewById(R.id.tv_location_toChange);
                tv10.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_location)));
                tv11 = (TextView) findViewById(R.id.tv_status_toChange);
                tv11.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_status)));
                tv12 = (TextView) findViewById(R.id.tv_type_toChange);
                tv12.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_circType)));
                tv13 = (TextView) findViewById(R.id.tv_volyear_toChange);
                tv13.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_volYear)));
                tv14 = (TextView) findViewById(R.id.tv_copies_toChange);
                tv14.setText(cursor.getString(cursor.getColumnIndex(dbHelper.col_noOfCopies)));
            }while(cursor.moveToNext());
        }

        bookId = BookDetail.tv1.getText().toString();
        numberOfCopies = Integer.parseInt(BookDetail.tv14.getText().toString());

    }

    public class CounterTask extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        TheCallback callback;
        String reservationsString;
        int len;

        public CounterTask(TheCallback tcb){
            this.callback = tcb;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BookDetail.context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Processing...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];

            try{
                URL link = new URL(url);
                HttpURLConnection con = (HttpURLConnection) link.openConnection();
                InputStream in = con.getInputStream();
                Scanner scanner = new Scanner(in).useDelimiter("\\A");
                reservationsString = scanner.hasNext() ? scanner.next() : "";

                JSONObject res = new JSONObject(reservationsString);
                len = res.length();
                Log.e("Json:", "Res Length: " + len);

                Iterator<String> iterator = res.keys();
                ArrayList<String> keys = new ArrayList<String>();
                if(iterator.hasNext()){
                    do{
                       keys.add(iterator.next());
                    }while(iterator.hasNext());
                }

                for(int i=0;i<keys.size();i++){
                    JSONObject obj = res.getJSONObject(keys.get(i));
                    if(client.getId().equals(obj.getString("userID")) && bookId.equals(obj.getString("bookId"))){
                        isAlreadyReserved = true;
                    }
                }

                BookDetail.numberOfReservations = len;
                con.disconnect();
                in.close();
                scanner.close();
            } catch(JSONException jsonEx){
                Log.e("JSON Err:", jsonEx.toString());
            } catch (MalformedURLException malform){
                Log.e("Malform Err: ", malform.toString());
            } catch (IOException ioe){
                Log.e("IOE Err", ioe.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(callback != null)
                this.callback.onCallback();

            progressDialog.dismiss();
        }
    }
}
