package com.example.jeobmallari.ilib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, OnClickListener {

    private static final int RC_SIGN_IN = 9001;

    private static final String TAG = "LoginActivity";

    private Context context = this;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
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
                signIn();
                break;
        }
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
            if(mGoogleApiClient.isConnected()){
//                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount acct = result.getSignInAccount();
                findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                SignedInGoogleClient user = SignedInGoogleClient.getOurInstance();
                user.setDisplayName(acct.getDisplayName());
                user.setEmail(acct.getEmail());
                user.setDisplayPic(acct.getPhotoUrl());
                user.setFamilyName(acct.getFamilyName());
                user.setGivenName(acct.getGivenName());
                user.setId(acct.getId());

                Intent intent = new Intent(context, Home.class);
                startActivity(intent);
            }
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            SignedInGoogleClient user = SignedInGoogleClient.getOurInstance();
            user.setDisplayName(acct.getDisplayName());
            user.setEmail(acct.getEmail());
            user.setDisplayPic(acct.getPhotoUrl());
            user.setFamilyName(acct.getFamilyName());
            user.setGivenName(acct.getGivenName());
            user.setId(acct.getId());

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);

            Intent intent = new Intent(context, Home.class);
            startActivity(intent);

        } else {
            // Signed out, show unauthenticated UI.
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

