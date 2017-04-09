package com.example.jeobmallari.ilib;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Scope;

import java.util.List;

/**
 * Created by Jeob Mallari on 4/8/2017.
 */

class SignedInGoogleClient {

    private String email = "";
    private String name = "";
    private String displayName= "";
    private String givenName = "";
    private String familyName ="";
    private String id = "";
    private Uri displayPic;
    private GoogleSignInAccount mGoogleSignInAcct;

    private static SignedInGoogleClient ourInstance = null;

    private SignedInGoogleClient(){}

    public static SignedInGoogleClient getOurInstance(){
        if(ourInstance == null){
            ourInstance = new SignedInGoogleClient();
        }
        return ourInstance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getDisplayPic() {
        return displayPic;
    }

    public void setDisplayPic(Uri displayPic) {
        this.displayPic = displayPic;
    }

    public GoogleSignInAccount getmGoogleSignInAcct() {
        return mGoogleSignInAcct;
    }

    public void setmGoogleSignInAcct(GoogleSignInAccount mGoogleSignInAcct) {
        this.mGoogleSignInAcct = mGoogleSignInAcct;
    }

    public static void setOurInstance(SignedInGoogleClient ourInstance) {
        SignedInGoogleClient.ourInstance = ourInstance;
    }
}
