package com.example.jeobmallari.ilib;

/**
 * Created by Jeob Mallari on 5/7/2017.
 */

public class Reservation {

    String bookId;
    String userID;
    String resID;

    public Reservation(){

    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getResID() {
        return resID;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

}
