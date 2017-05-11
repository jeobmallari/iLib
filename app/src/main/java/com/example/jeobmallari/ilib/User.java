package com.example.jeobmallari.ilib;

import android.net.Uri;

/**
 * Created by Jeob Mallari on 5/4/2017.
 */

public class User {
    private String email = "";
    private String name = "";
    private String displayName= "";
    private String givenName = "";
    private String familyName ="";
    private String id = "";
    private String displayPic = "";

    private String studentNo = "";
    private String homeadd = "";
    private String collegeadd = "";
    private String bday = "";
    private String college = "";

    public void setCollege(String college){
        this.college = college;
    }

    public String getCollege(){
        return this.college;
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

    public User(){}

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

    public String getDisplayPic() {
        return displayPic;
    }

    public void setDisplayPic(String displayPic) {
        this.displayPic = displayPic;
    }
}
