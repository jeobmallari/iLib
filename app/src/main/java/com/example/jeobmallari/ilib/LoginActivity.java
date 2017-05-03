package com.example.jeobmallari.ilib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;
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
import static com.example.jeobmallari.ilib.DBHelper.bookTableName;
import static com.example.jeobmallari.ilib.DBHelper.res_user_id;
import static com.example.jeobmallari.ilib.DBHelper.reserveTableName;
import static com.example.jeobmallari.ilib.DBHelper.reserve_id;
import static com.example.jeobmallari.ilib.DBHelper.userTableName;
import static com.example.jeobmallari.ilib.DBHelper.user_col_email;
import static com.example.jeobmallari.ilib.DBHelper.user_col_familyName;
import static com.example.jeobmallari.ilib.DBHelper.user_col_id;
import static com.example.jeobmallari.ilib.DBHelper.user_col_name;
import static com.example.jeobmallari.ilib.DBHelper.user_col_displayname;
import static com.example.jeobmallari.ilib.DBHelper.user_col_givenName;
import static com.example.jeobmallari.ilib.DBHelper.user_col_displayPic;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, OnClickListener {

    private static final int RC_SIGN_IN = 9001;

    private static final String TAG = "LoginActivity";

    public Context context = this;
    public static GoogleApiClient mGoogleApiClient;

    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    final String firebaseBaseURL = "https://fir-milib.firebaseio.com/.json";
    final String printArg = "print";
    static String json = "";
    SignedInGoogleClient user;
    Intent datum;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child(dbHelper.userTableName);
    static ArrayList<String> userIDCollection = new ArrayList<String>();
    User dbUser;

    public interface FirebaseCallback{
        void onRespond();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton sib = (SignInButton) findViewById(R.id.sign_in_button);
        sib.setSize(SignInButton.SIZE_STANDARD);
        sib.setOnClickListener(this);

    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.sign_in_button:
                if(isNetworkAvailable()){
                    signIn();
                }
                else{
                    String internet = (String) this.getResources().getString(R.string.internetConnectionRequired);
                    Toast.makeText(this, internet, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
//            if(mGoogleApiClient.isConnected()){
//                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                        new ResultCallback<Status>() {
//                            @Override
//                            public void onResult(@NonNull Status status) {
//                                // wala lang.
//                            }
//                        }
//                );
//            }
            datum = data;
            FirebaseCallback fbc = new FirebaseCallback() {
                @Override
                public void onRespond() {
                    Log.e("Callback: ", "Back to Main UI Thread");
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(datum);
                    handleSignInResult(result);
                }
            };

            FireBaseQueryTask fbqt = new FireBaseQueryTask(fbc);
            fbqt.execute(buildURL(firebaseBaseURL, printArg));
        }
    }

    public class FireBaseQueryTask extends AsyncTask<URL, Void, String>{
        String jsonString = "";
        ProgressDialog progressDialog;
        FirebaseCallback callback;

        public FireBaseQueryTask(FirebaseCallback fbc){
            callback = fbc;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Getting Ready...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL restURL = urls[0];
            try {
                HttpURLConnection urlCon = (HttpURLConnection) restURL.openConnection();
                InputStream in = urlCon.getInputStream();
                Scanner scan = new Scanner(in).useDelimiter("\\A");
                jsonString = scan.hasNext()? scan.next() : "";
                LoginActivity.json = jsonString;
                LoginActivity.dbHelper = DBHelper.getInstance(LoginActivity.this);
                LoginActivity.db = LoginActivity.dbHelper.getWritableDatabase();
                LoginActivity.db.execSQL("DROP TABLE IF EXISTS "+dbHelper.bookTableName);
                LoginActivity.db.execSQL(dbHelper.createCommand);
                LoginActivity.db.execSQL("DROP TABLE IF EXISTS "+dbHelper.userTableName);
                LoginActivity.db.execSQL(dbHelper.createUserTable);
                LoginActivity.db.execSQL("DROP TABLE IF EXISTS "+dbHelper.reserveTableName);
                LoginActivity.db.execSQL(dbHelper.createReservations);
                //--------------------------------------------
                try{
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray bookArr = jsonObject.getJSONArray(bookTableName);
                    for(int i=0;i<bookArr.length();i++){
                        //Log.e("ARR #"+(i+1), bookArr.getJSONObject(i).toString());
                        JSONObject bookInstance = bookArr.getJSONObject(i);
                        ContentValues values = new ContentValues();
                        String accNo = bookInstance.getString(col_accessionNo);
                        values.put(col_accessionNo, accNo);
                        String auth = bookInstance.getString(col_author);
                        values.put(col_author, auth);
                        int id = Integer.parseInt(bookInstance.getString(col_bookId));
                        values.put(col_bookId, id);
                        String cNo = bookInstance.getString(col_callNo);
                        values.put(col_callNo, cNo);
                        String ct = bookInstance.getString(col_circType);
                        values.put(col_circType, ct);
                        String db1 = bookInstance.getString(col_dateBorrowed);
                        values.put(col_dateBorrowed, db1);
                        String dd = bookInstance.getString(col_dueDate);
                        values.put(col_dueDate, dd);
                        String fmt = bookInstance.getString(col_format);
                        values.put(col_format, fmt);
                        String isbn = bookInstance.getString(col_isbnNo);
                        values.put(col_isbnNo, isbn);
                        String loc = bookInstance.getString(col_location);
                        values.put(col_location, loc);
                        int num = Integer.parseInt(bookInstance.getString(col_noOfCopies));
                        values.put(col_noOfCopies, num);
                        String pub = bookInstance.getString(col_publisher);
                        values.put(col_publisher, pub);
                        String stat = bookInstance.getString(col_status);
                        values.put(col_status, stat);
                        String subj = bookInstance.getString(col_subject);
                        values.put(col_subject, subj);
                        String title = bookInstance.getString(col_title);
                        values.put(col_title, title);
                        int vy = Integer.parseInt(bookInstance.getString(col_volYear));
                        values.put(col_volYear, vy);

                        LoginActivity.db.insert(dbHelper.bookTableName, null, values);
                    }

                    JSONArray users = jsonObject.getJSONArray(userTableName);
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject userInstance = users.getJSONObject(i);
                        ContentValues valuePairs = new ContentValues();
                        String id = userInstance.getString(user_col_id);
                        valuePairs.put(user_col_id, id);
                        String name = userInstance.getString(user_col_name);
                        valuePairs.put(user_col_name, name);
                        String famname = userInstance.getString(user_col_familyName);
                        valuePairs.put(user_col_familyName, famname);
                        String dispname = userInstance.getString(user_col_displayname);
                        valuePairs.put(user_col_name, dispname);
                        String givenname = userInstance.getString(user_col_givenName);
                        valuePairs.put(user_col_givenName, givenname);
                        String email = userInstance.getString(user_col_email);
                        valuePairs.put(user_col_email, email);
                        String dp = userInstance.getString(user_col_displayPic);
                        valuePairs.put(user_col_displayPic, dp);
                        LoginActivity.db.insert(dbHelper.userTableName, null, valuePairs);
                    }

                    JSONArray reservations = jsonObject.getJSONArray(reserveTableName);
                    for(int a=0;a<reservations.length();a++){
                        JSONObject res = reservations.getJSONObject(a);
                        ContentValues cv = new ContentValues();
                        String resid = res.getString(reserve_id);
                        cv.put(reserve_id, resid);
                        String bookid = res.getString(col_bookId);
                        cv.put(col_bookId, bookid);
                        String userid = res.getString(res_user_id);
                        cv.put(res_user_id, userid);
                        LoginActivity.db.insert(reserveTableName, null, cv);
                    }


                    Log.e("Insert to db: ", "Successful! ");
                    urlCon.disconnect();
                    in.close();
                    Thread.sleep(2000);
                } catch(SQLiteException e){
                    Log.e("SQLite Err", "@LoginActivity\n"+jsonString);
                    e.printStackTrace();
                } catch(JSONException jsonEx){
                    Log.e("JSON Err", "Look up");
                    jsonEx.printStackTrace();
                } catch(InterruptedException interrupt){
                    interrupt.printStackTrace();
                }

                //----------------------------------------------------

                return jsonString;
            } catch(IOException ioe){
                ioe.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(callback != null){
                callback.onRespond();
            }
            progressDialog.dismiss();
        }
    }

    public URL buildURL(String toBeURL, String printarg){
        Uri builtURI = Uri.parse(toBeURL).buildUpon()
                .appendQueryParameter(printarg, "pretty")
                .build();
        URL url = null;
        try{
            url = new URL(builtURI.toString());
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.e(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            user = SignedInGoogleClient.getOurInstance();
            user.setDisplayName(acct.getDisplayName());
            user.setEmail(acct.getEmail());
            user.setDisplayPic(acct.getPhotoUrl());
            user.setFamilyName(acct.getFamilyName());
            user.setGivenName(acct.getGivenName());
            user.setId(acct.getId());
            user.setmGoogleClient(mGoogleApiClient);
            user.setDBHelper(dbHelper);

            dbUser = new User();
            dbUser.setId(user.getId());
            dbUser.setFamilyName(user.getFamilyName());
            dbUser.setDisplayName(user.getDisplayName());
            dbUser.setEmail(user.getEmail());
            dbUser.setGivenName(user.getGivenName());
            dbUser.setName(user.getName());
            if(user.getDisplayPic() != null)
                dbUser.setDisplayPic(user.getDisplayPic().toString());
            else dbUser.setDisplayPic("");

            FirebaseDatabase.getInstance().getReference().child(dbHelper.userTableName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        int i=0;
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                userIDCollection.add(user.getId());
                                Log.e("userID collection", "added from rtdb: "+user.getId());
                                if(dbUser.getId().equals(user.getId())){
                                    mUserRef.child(dbUser.getId()).setValue(dbUser); // user already exists in db
                                    break;
                                }
                                i++;
                            }
                            Log.e("userID collection", "new user: "+dbUser.getId());
                            mUserRef.child(dbUser.getId()).setValue(dbUser);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);

            Intent intent = new Intent(context, Home.class);
            startActivity(intent);
            finish();

        } else {
            // Signed out, show unauthenticated UI.
            Log.e(TAG, "Login err: "+result.isSuccess());
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            String prompt = "Failed logging in.";
            Toast.makeText(this, prompt, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

}

