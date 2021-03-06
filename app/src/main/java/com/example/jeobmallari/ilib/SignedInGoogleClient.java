package com.example.jeobmallari.ilib;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
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
    private String studentNo = "";
    private String homeadd = "";
    private String collegeadd = "";
    private String bday = "";
    private Uri displayPic;
    private GoogleSignInAccount mGoogleSignInAcct;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private String college = "";

    public void setCollege(String college){
        this.college = college;
    }

    public String getCollege(){
        return this.college;
    }

    private static SignedInGoogleClient ourInstance = null;

    private SignedInGoogleClient(){}

    public static SignedInGoogleClient getOurInstance(){
        if(ourInstance == null){
            ourInstance = new SignedInGoogleClient();
        }
        return ourInstance;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getHomeadd() {
        return homeadd;
    }

    public void setHomeadd(String homeadd) {
        this.homeadd = homeadd;
    }

    public String getCollegeadd() {
        return collegeadd;
    }

    public void setCollegeadd(String collegeadd) {
        this.collegeadd = collegeadd;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public void setDb(SQLiteDatabase database){
        this.db = database;
    }

    public SQLiteDatabase getDb(){
        return this.db;
    }

    public void setDBHelper(DBHelper dbh){
        this.dbHelper = dbh;
    }

    public DBHelper getDBHelper(){
        return this.dbHelper;
    }

    public GoogleApiClient getmGoogleClient() {
        return mGoogleClient;
    }

    public void setmGoogleClient(GoogleApiClient mGoogleClient) {
        this.mGoogleClient = mGoogleClient;
    }

    private GoogleApiClient mGoogleClient;

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
